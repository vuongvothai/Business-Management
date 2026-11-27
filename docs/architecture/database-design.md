# Thiết kế cơ sở dữ liệu

## Audit log

`audit_log` is append-only. Migration V12 adds database triggers rejecting UPDATE and DELETE; the application exposes only the ADMIN read endpoint.

## Nguyên tắc

- Sử dụng MySQL.
- Khóa chính dùng số nguyên tự tăng.
- Khối lượng, đơn giá và thành tiền dùng `DECIMAL`, không dùng `FLOAT`.
- Tên cột trong database dùng `snake_case`; API chuyển sang `camelCase`.
- Tạo index cho các khóa ngoại và các cột thường dùng để lọc: `import_date`,
  `supplier_id`, `item_id`.
- Các bảng giao dịch chính lưu `created_at`, `created_by`, `updated_at` và
  `updated_by` để phục vụ audit.

## Mô hình dữ liệu và trạng thái triển khai

Các bảng dưới đây mô tả schema mục tiêu theo ERD mới. Migration sẽ viết lại
từ `V1` (`V1`–`V12`), không giữ `V1`–`V13` cũ.

### `supplier`

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `supplier_id` | `BIGINT` | PK |
| `name` | `VARCHAR(255)` | NOT NULL |
| `identity_no` | `VARCHAR(50)` | NULL, CCCD hoặc mã định danh |
| `phone` | `VARCHAR(30)` | NOT NULL |
| `address` | `VARCHAR(500)` | NULL |
| `usable_coffee_kg` | `DECIMAL(14,3)` | NOT NULL, >= 0 |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

`identity_no` không đặt `UNIQUE` và cho phép `NULL` vì nhà cung cấp có thể là
nông hộ, doanh nghiệp hoặc hợp tác xã. Có thể dùng CCCD, mã số thuế hoặc mã
định danh phù hợp với loại nhà cung cấp.

`usable_coffee_kg` lưu trực tiếp trên nhà cung cấp. Khi phiếu nhập được xác
nhận, hệ thống cộng khối lượng hữu dụng; khi chốt giá, hệ thống trừ trực tiếp
trong transaction. Trường này là số dư tổng của nhà cung cấp, không phân tách
theo mặt hàng.

### `item`

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `item_id` | `BIGINT` | PK |
| `name` | `VARCHAR(255)` | NOT NULL, ví dụ `Cà phê nhân` |
| `category` | `VARCHAR(50)` | NOT NULL, ví dụ `Loại 1` |
| `unit` | `VARCHAR(20)` | NOT NULL, mặc định `KG` |
| `available_kg` | `DECIMAL(14,3)` | NOT NULL, có thể âm |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

### `vehicle`

`vehicle` chỉ lưu danh sách biển số xe dùng lại trên phiếu nhập và phiếu giao;
không quản lý chủ xe, loại xe hoặc thông tin vận tải khác.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `vehicle_id` | `BIGINT` | PK |
| `license_plate` | `VARCHAR(30)` | NOT NULL, UNIQUE |
| `active` | `BOOLEAN` | NOT NULL, mặc định `TRUE` |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

Không xóa cứng biển số đã được dùng trong phiếu nhập; thao tác xóa trên giao
diện chuyển `active` thành `FALSE` để không còn xuất hiện trong danh sách chọn,
nhưng vẫn bảo toàn lịch sử.

### `role` và `user`

`role` chuẩn hóa vai trò, tránh sai khác như `Admin`, `ADMIN` và `admin`.

| Cột `role` | Kiểu | Ràng buộc |
|---|---|---|
| `role_id` | `BIGINT` | PK |
| `role_name` | `VARCHAR(30)` | NOT NULL, UNIQUE: `ADMIN`, `WEIGHER`, `ACCOUNTANT` |

| Cột `user` | Kiểu | Ràng buộc |
|---|---|---|
| `user_id` | `BIGINT` | PK |
| `full_name` | `VARCHAR(255)` | NOT NULL |
| `role_id` | `BIGINT` | FK tới `role.role_id`, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

Quyền nghiệp vụ:

| Vai trò | Quyền |
|---|---|
| `ADMIN` | Toàn quyền |
| `WEIGHER` | Tạo phiếu nhập, xem các phiếu do mình tạo |
| `ACCOUNTANT` | Xem, chốt giá, xuất hàng |

### `import_ticket`

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `import_id` | `BIGINT` | PK |
| `supplier_id` | `BIGINT` | FK, NOT NULL |
| `item_id` | `BIGINT` | FK, NOT NULL |
| `vehicle_id` | `BIGINT` | FK tới `vehicle.vehicle_id`, NULL |
| `truck_license_plate` | `VARCHAR(30)` | NULL, biển số xe tại thời điểm nhập |
| `loading_warehouse` | `VARCHAR(255)` | NULL |
| `import_warehouse` | `VARCHAR(255)` | NULL |
| `number_of_bags` | `INT` | NOT NULL, > 0, backend tính từ `IMPORT_WEIGHT` |
| `total_kg` | `DECIMAL(14,3)` | NOT NULL, > 0, backend tính từ `IMPORT_WEIGHT` |
| `tare_weight` | `DECIMAL(14,3)` | NULL, >= 0 |
| `sample_test_result` | `VARCHAR(1000)` | NULL |
| `moisture_percent` | `DECIMAL(5,2)` | NULL, từ 0 đến 100 |
| `impurity_percent` | `DECIMAL(5,2)` | NULL, từ 0 đến 100 |
| `photo_url` | `VARCHAR(1000)` | NULL, không bắt buộc |
| `import_date` | `DATE` | NOT NULL |
| `note` | `VARCHAR(1000)` | NULL |
| `created_at` | `DATETIME` | NOT NULL |
| `created_by` | `BIGINT` | FK tới `user.user_id`, NOT NULL |
| `status` | `VARCHAR(20)` | NOT NULL: `DRAFT`, `CONFIRMED`, `CANCELLED` |
| `updated_at` | `DATETIME` | NOT NULL |

`number_of_bags` và `total_kg` được lưu trên phiếu nhập để truy vấn nhanh,
nhưng luôn do backend tính từ các bản ghi `IMPORT_WEIGHT` trong cùng
transaction. Client không được tự ghi đè hai giá trị này.
`vehicle_id` phục vụ chọn nhanh; `truck_license_plate` là snapshot để lịch sử
không thay đổi khi bản ghi biển số được cập nhật hoặc ẩn khỏi danh sách.

Các cột mở rộng phiếu nhập theo ERD mới.

### `import_weight`

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `weight_id` | `BIGINT` | PK |
| `import_id` | `BIGINT` | FK, NOT NULL |
| `weight_kg` | `DECIMAL(12,3)` | NOT NULL, > 0 |
| `bag_count` | `TINYINT` | NOT NULL, chỉ nhận `1` hoặc `2` |
| `created_by` | `BIGINT` | FK tới `user.user_id`, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |

Mỗi dòng là một lần cân thuộc phiếu nhập và có thể đại diện cho một hoặc hai
bao. Khi tạo hoặc sửa phiếu nhập:

- `SUM(import_weight.bag_count)` phải bằng `import_ticket.number_of_bags`;
- `SUM(import_weight.weight_kg)` phải bằng `import_ticket.total_kg`;
- `bag_count` chỉ nhận giá trị `1` hoặc `2`.

### `price_lock`

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `price_lock_id` | `BIGINT` | PK |
| `supplier_id` | `BIGINT` | FK, NOT NULL |
| `item_id` | `BIGINT` | FK, NOT NULL |
| `locked_volume` | `DECIMAL(14,3)` | NOT NULL, > 0 |
| `lock_date` | `DATE` | NOT NULL |
| `locked_price_per_kg` | `DECIMAL(15,2)` | NOT NULL, > 0 |
| `amount` | `DECIMAL(18,2)` | NOT NULL, >= 0 |
| `note` | `VARCHAR(1000)` | NULL |
| `status` | `VARCHAR(20)` | NOT NULL: `DRAFT`, `CONFIRMED` |
| `created_at` | `DATETIME` | NOT NULL |
| `created_by` | `BIGINT` | NULL |
| `updated_at` | `DATETIME` | NOT NULL |
| `updated_by` | `BIGINT` | NULL |

`PRICE_LOCK` gắn với đúng một `ITEM`, lưu `locked_volume` và `amount`.
Phiếu chốt không phụ thuộc vào một `IMPORT_TICKET` cụ thể; số dư được trừ
trực tiếp từ `supplier.usable_coffee_kg`. Backend tính `amount` bằng
`locked_volume * locked_price_per_kg` trong cùng transaction.

### `customer`

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `customer_id` | `BIGINT` | PK |
| `name` | `VARCHAR(255)` | NOT NULL |
| `phone` | `VARCHAR(30)` | NULL |
| `address` | `VARCHAR(500)` | NULL |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

### `export_ticket` và `export_delivery`

`export_ticket` là phiếu chốt bán, có thể dùng làm căn cứ lập hóa đơn điện tử.
Mỗi phiếu gắn với đúng một `item_id` và có các trạng thái `DRAFT`,
`CONFIRMED`, `PARTIALLY_DELIVERED`, `COMPLETED`.
`committed_volume` là tổng khối lượng cam kết bán, không phải khối lượng đã
giao. Phiếu không phụ thuộc trực tiếp vào `IMPORT_TICKET`.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `export_id` | `BIGINT` | PK |
| `customer_id` | `BIGINT` | FK, NOT NULL |
| `item_id` | `BIGINT` | FK, NOT NULL |
| `ticket_type` | `VARCHAR(20)` | NOT NULL, mặc định `DIRECT`: `DIRECT`, `LOCKED`, `FROM_CONSIGNMENT` — **kế hoạch**, xem [README.md](../design/README.md) |
| `weigh_ticket_id` | `BIGINT` | FK tới `weigh_ticket`, NULL — **kế hoạch** |
| `warehouse_id` | `BIGINT` | FK tới `warehouse`, NULL — **kế hoạch** |
| `customer_consignment_id` | `BIGINT` | FK tới `customer_consignment_ticket`, NULL, bắt buộc khi `FROM_CONSIGNMENT` — **kế hoạch** |
| `deposit_amount` | `DECIMAL(18,2)` | NOT NULL, mặc định 0 — **kế hoạch** |
| `customer_reported_kg` | `DECIMAL(14,3)` | NULL, khối lượng quy chuẩn khách báo lại — **kế hoạch** |
| `diff_kg` | `DECIMAL(14,3)` | NULL, tự tính = `delivered − customer_reported` — **kế hoạch** |
| `committed_volume` | `DECIMAL(14,3)` | NOT NULL, > 0 |
`delivery_due_date` không còn thuộc mô hình hiện tại; ngày giao thực tế nằm ở
`export_delivery.delivery_date`.

`export_delivery` lưu từng lần giao hàng thực tế:

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `delivery_id` | `BIGINT` | PK |
| `export_id` | `BIGINT` | FK, NOT NULL |
| `vehicle_id` | `BIGINT` | FK tới `vehicle.vehicle_id`, NULL |
| `truck_license_plate` | `VARCHAR(30)` | NULL, snapshot biển số tại thời điểm giao |
| `loading_warehouse` | `VARCHAR(255)` | NULL |
| `import_warehouse` | `VARCHAR(255)` | NULL |
| `delivered_volume` | `DECIMAL(14,3)` | NOT NULL, > 0 |
| `delivery_date` | `DATE` | NOT NULL |
| `truck_license_plate` | `VARCHAR(30)` | NULL, snapshot biển số tại thời điểm giao |
| `status` | `VARCHAR(20)` | NOT NULL: `CONFIRMED`, `CANCELLED` |
| `created_by` | `BIGINT` | FK, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |

`delivered_volume` được tính bằng tổng các lần giao không bị hủy.
`remaining_volume = committed_volume - delivered_volume`. Khi xác nhận một
`EXPORT_DELIVERY`, backend khóa `ITEM` trong transaction và trừ
`delivered_volume` khỏi `ITEM.available_kg`. Tạo phiếu cam kết không làm thay
đổi tồn kho. Cam kết bán có thể vượt tồn hiện tại.

Các cột `ticket_type`, `weigh_ticket_id`, `warehouse_id`,
`customer_consignment_id`, `deposit_amount`, `customer_reported_kg` và
`diff_kg` thuộc ERD mới. ERD thay
`export_ticket`/`export_delivery` bằng `SALES_CONTRACT`/`SALES_DELIVERY`; xem
[erd.mmd](./erd.mmd) và [design/README.md](../design/README.md) mục 2.

`vehicle_id`, `truck_license_plate`, `loading_warehouse` và `import_warehouse`
được lưu trên từng lần giao. Snapshot biển số phải được lưu độc lập để giữ
nguyên lịch sử dù xe bị đổi tên hoặc ẩn.

### `audit_log`

Lưu vết thao tác nghiệp vụ với người thực hiện, loại thao tác, bản ghi liên quan,
chi tiết tóm tắt và thời điểm. Audit log không thay thế các cột `created_by`/
`updated_by` trên chứng từ.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `audit_id` | `BIGINT` | PK |
| `user_id` | `BIGINT` | FK tới `app_user.user_id`, NOT NULL |
| `action` | `VARCHAR(30)` | NOT NULL |
| `entity_type` | `VARCHAR(50)` | NOT NULL |
| `entity_id` | `VARCHAR(100)` | NOT NULL |
| `details` | `VARCHAR(1000)` | NULL |
| `created_at` | `DATETIME` | NOT NULL |

### `expense` và `expense_detail`

Không tách bảng danh mục chi phí độc lập. Tên danh mục được lưu trực tiếp ở
`expense_detail.category_name`; một `EXPENSE` có thể có nhiều dòng danh mục.

`expense.total_amount` là tổng tiền của nhiều danh mục trong một khoảng thời
gian, dùng `period_start` và `period_end`; backend phải kiểm tra:

`expense.total_amount = SUM(expense_detail.amount)`

Tạo `UNIQUE(expense_id, category_name)` để một kỳ chi phí không có hai dòng
trùng danh mục.

| Bảng/cột | Kiểu | Ràng buộc |
|---|---|---|
| `expense.expense_id` | `BIGINT` | PK |
| `expense.total_amount` | `DECIMAL(18,2)` | NOT NULL, >= 0 |
| `expense.period_start` | `DATE` | NOT NULL |
| `expense.period_end` | `DATE` | NOT NULL, >= `period_start` |
| `expense_detail.expense_detail_id` | `BIGINT` | PK |
| `expense_detail.expense_id` | `BIGINT` | FK, NOT NULL |
| `expense_detail.category_name` | `VARCHAR(100)` | NOT NULL |
| `expense_detail.amount` | `DECIMAL(18,2)` | NOT NULL, >= 0 |

### `market_price`

`market_price.item_id` là khóa ngoại tới `item.item_id`. Nên tạo unique key
`UNIQUE(item_id, price_date)` để mỗi mặt hàng chỉ có một giá thị trường trong
một ngày. Để hiển thị giá hôm nay có thể truy xuất nguồn gốc, bản ghi giá phải
có `source` và thời điểm ghi nhận; khi nhập thủ công, thời điểm này là lúc lưu.
Schema hiện tại cho phép các cột này `NULL`, nên cần quyết định backfill và
siết ràng buộc trong migration nếu dùng làm dữ liệu dashboard.

### `consignment_ticket`

Phiếu nhập gửi kho (ký gửi chờ chốt giá). Khác với `import_ticket` ở chỗ quyền
sở hữu hàng vẫn thuộc về khách (`customer_id`) cho đến khi chốt giá. Hàng gửi
kho không làm thay đổi `supplier.usable_coffee_kg` hay `item.available_kg`.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `consignment_id` | `BIGINT` | PK |
| `customer_id` | `BIGINT` | FK tới `customer.customer_id`, NOT NULL |
| `item_id` | `BIGINT` | FK, NOT NULL |
| `vehicle_id` | `BIGINT` | FK tới `vehicle.vehicle_id`, NULL |
| `truck_license_plate` | `VARCHAR(30)` | NULL, snapshot biển số |
| `number_of_bags` | `INT` | NOT NULL, > 0, backend tính từ `CONSIGNMENT_WEIGHT` |
| `gross_weight_kg` | `DECIMAL(14,3)` | NOT NULL, > 0, trọng lượng có bì |
| `tare_weight_kg` | `DECIMAL(14,3)` | NOT NULL, >= 0, trọng lượng bì |
| `net_weight_kg` | `DECIMAL(14,3)` | NOT NULL, > 0, `gross_weight_kg - tare_weight_kg` |
| `moisture_percent` | `DECIMAL(5,2)` | NULL, từ 0 đến 100 |
| `impurity_percent` | `DECIMAL(5,2)` | NULL, từ 0 đến 100 |
| `black_broken_ratio` | `DECIMAL(5,2)` | NULL, tỷ lệ đen/vỡ/sâu (%) |
| `screen_size` | `VARCHAR(20)` | NULL, R13, R16, R18... |
| `standard_weight_kg` | `DECIMAL(14,3)` | NOT NULL, >= 0, **trọng lượng quy chuẩn**, tự tính |
| `moisture_standard` | `DECIMAL(5,2)` | NOT NULL, mặc định 12.5, chuẩn độ ẩm |
| `impurity_standard` | `DECIMAL(5,2)` | NOT NULL, mặc định 1.0, chuẩn tạp chất |
| `free_storage_days` | `INT` | NOT NULL, mặc định 0, miễn phí lưu kho X ngày |
| `storage_fee_per_ton_month` | `DECIMAL(12,2)` | NULL, phí lưu kho/tấn/tháng sau miễn phí |
| `natural_loss_rate` | `DECIMAL(5,2)` | NULL, tỷ lệ hao hụt tự nhiên/tháng (%) |
| `photo_url` | `VARCHAR(1000)` | NULL |
| `consignment_date` | `DATE` | NOT NULL, ngày nhập gửi |
| `note` | `VARCHAR(1000)` | NULL |
| `status` | `VARCHAR(20)` | NOT NULL: `DRAFT`, `CONFIRMED`, `CANCELLED` |
| `created_by` | `BIGINT` | FK tới `user.user_id`, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

**Trọng lượng quy chuẩn** (`standard_weight_kg`) là cơ sở tính tiền duy nhất
cho hàng gửi kho, được tính tự động:

`standard_weight_kg = net_weight_kg × (1 - max(0, moisture_percent - moisture_standard)/100) × (1 - max(0, impurity_percent - impurity_standard)/100)`

Backend phải tính `standard_weight_kg` trong cùng transaction khi xác nhận
phiếu; client không được ghi đè.

### `consignment_weight`

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `cw_id` | `BIGINT` | PK |
| `consignment_id` | `BIGINT` | FK, NOT NULL |
| `weight_kg` | `DECIMAL(12,3)` | NOT NULL, > 0 |
| `bag_count` | `TINYINT` | NOT NULL, >= 1 |
| `created_by` | `BIGINT` | FK, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |

Tương tự `IMPORT_WEIGHT`, mỗi dòng là một lần cân. `SUM(weight_kg)` phải
bằng `consignment_ticket.gross_weight_kg`.

### `consignment_balance`

Sổ Hàng Gửi Kho (Ledger 1) — theo dõi số dư hàng gửi theo từng khách hàng
và mặt hàng.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `balance_id` | `BIGINT` | PK |
| `customer_id` | `BIGINT` | FK, NOT NULL |
| `item_id` | `BIGINT` | FK, NOT NULL |
| `total_sent_kg` | `DECIMAL(14,3)` | NOT NULL, >= 0, tổng đã gửi (quy chuẩn) |
| `total_locked_kg` | `DECIMAL(14,3)` | NOT NULL, >= 0, tổng đã chốt giá |
| `total_withdrawn_kg` | `DECIMAL(14,3)` | NOT NULL, >= 0, tổng đã rút |
| `remaining_kg` | `DECIMAL(14,3)` | NOT NULL, >= 0, **tự tính** = `total_sent_kg - total_locked_kg - total_withdrawn_kg` |
| `updated_at` | `DATETIME` | NOT NULL |

Tạo `UNIQUE(customer_id, item_id)` để mỗi khách chỉ có một số dư cho mỗi mặt
hàng. Backend cập nhật trong transaction khi xác nhận nhập gửi, chốt giá, hoặc
rút hàng.

### `consignment_price_lock`

Phiếu chốt giá từ hàng gửi kho. Khác với `PRICE_LOCK` (chốt giá hàng mua đứt)
ở chỗ: gắn với `customer_id` và `consignment_id`, có đối trừ tạm ứng, và lưu
ngày giờ đến phút/giây.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `cpl_id` | `BIGINT` | PK |
| `customer_id` | `BIGINT` | FK, NOT NULL |
| `consignment_id` | `BIGINT` | FK, NOT NULL, phiếu gửi gốc |
| `item_id` | `BIGINT` | FK, NOT NULL |
| `locked_volume` | `DECIMAL(14,3)` | NOT NULL, > 0, khối lượng chốt (quy chuẩn) |
| `locked_at` | `DATETIME` | NOT NULL, ngày giờ chốt đến phút/giây |
| `locked_price_per_kg` | `DECIMAL(15,2)` | NOT NULL, > 0 |
| `gross_amount` | `DECIMAL(18,2)` | NOT NULL, >= 0, `locked_volume * locked_price_per_kg` |
| `advance_deduction_amount` | `DECIMAL(18,2)` | NOT NULL, mặc định 0, đối trừ tạm ứng (gốc + lãi) |
| `net_amount` | `DECIMAL(18,2)` | NOT NULL, >= 0, `gross_amount - advance_deduction_amount` |
| `note` | `VARCHAR(1000)` | NULL |
| `status` | `VARCHAR(20)` | NOT NULL: `DRAFT`, `CONFIRMED` |
| `created_by` | `BIGINT` | FK, NULL |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

Backend kiểm tra `locked_volume <= consignment_balance.remaining_kg` trong
transaction. Khi xác nhận, trừ `consignment_balance.total_locked_kg` và cập
nhật `remaining_kg`.

### `advance_payment`

Phiếu ứng tiền trên hàng đã gửi kho (Trường hợp A). Gắn với `customer_id` và
`consignment_id`, tính lãi suất theo ngày.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `advance_id` | `BIGINT` | PK |
| `customer_id` | `BIGINT` | FK, NOT NULL |
| `consignment_id` | `BIGINT` | FK, NOT NULL, phiếu gửi làm cơ sở |
| `advance_amount` | `DECIMAL(18,2)` | NOT NULL, > 0, số tiền ứng |
| `advance_date` | `DATE` | NOT NULL, ngày ứng |
| `interest_rate` | `DECIMAL(8,4)` | NOT NULL, > 0, lãi suất/tháng (ex: 0.8) |
| `max_advance_percent` | `INT` | NOT NULL, 0-100, hạn mức ứng tối đa (%) |
| `market_price_at_advance` | `DECIMAL(15,2)` | NOT NULL, giá thị trường tại thời điểm ứng |
| `interest_days` | `INT` | NOT NULL, >= 0, số ngày tính lãi (đến ngày chốt) |
| `interest_amount` | `DECIMAL(18,2)` | NOT NULL, >= 0, `advance_amount * interest_rate / 30 * interest_days` |
| `total_repayment` | `DECIMAL(18,2)` | NOT NULL, >= 0, `advance_amount + interest_amount` |
| `status` | `VARCHAR(20)` | NOT NULL: `ACTIVE`, `SETTLED` |
| `created_by` | `BIGINT` | FK, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

Backend kiểm tra `advance_amount <= consignment_balance.remaining_kg * market_price_at_advance * max_advance_percent / 100` khi tạo. Khi chốt giá, `interest_days` được cập nhật và `interest_amount` tính lại.

### `forward_contract`

Phiếu chốt giá trước – Giao hàng sau (Trường hợp B / Hợp đồng bao tiêu).
Sổ Hợp Đồng / Chốt Trước (Ledger 2).

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `fc_id` | `BIGINT` | PK |
| `customer_id` | `BIGINT` | FK, NOT NULL |
| `item_id` | `BIGINT` | FK, NOT NULL |
| `contract_code` | `VARCHAR(50)` | NOT NULL, UNIQUE, mã hợp đồng |
| `committed_volume` | `DECIMAL(14,3)` | NOT NULL, > 0, khối lượng cam kết (quy chuẩn) |
| `locked_price_per_kg` | `DECIMAL(15,2)` | NOT NULL, > 0, đơn giá đã chốt |
| `delivery_deadline` | `DATE` | NOT NULL, hạn chót giao hàng |
| `deposit_amount` | `DECIMAL(18,2)` | NOT NULL, >= 0, số tiền đặt cọc |
| `delivered_volume` | `DECIMAL(14,3)` | NOT NULL, mặc định 0, khối lượng đã giao |
| `remaining_volume` | `DECIMAL(14,3)` | NOT NULL, **tự tính** = `committed_volume - delivered_volume` |
| `status` | `VARCHAR(20)` | NOT NULL: `ACTIVE`, `COMPLETED`, `OVERDUE`, `CANCELLED` |
| `created_by` | `BIGINT` | FK, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |
| `updated_at` | `DATETIME` | NOT NULL |

### `forward_contract_delivery`

Giao hàng theo hợp đồng bao tiêu. Mapping nhiều phiếu cân vào 1 phiếu chốt.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `fcd_id` | `BIGINT` | PK |
| `fc_id` | `BIGINT` | FK, NOT NULL |
| `consignment_weight_id` | `BIGINT` | FK tới `consignment_weight.cw_id`, NULL, phiếu cân thực tế |
| `delivered_volume` | `DECIMAL(14,3)` | NOT NULL, > 0, khối lượng giao (quy chuẩn) |
| `delivery_date` | `DATE` | NOT NULL |
| `excess_volume` | `DECIMAL(14,3)` | NOT NULL, mặc định 0, phần giao dư |
| `excess_handling` | `VARCHAR(30)` | NULL: `MARKET_PRICE`, `NEW_CONSIGNMENT` |
| `note` | `VARCHAR(1000)` | NULL |
| `created_by` | `BIGINT` | FK, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |

Backend kiểm tra `SUM(delivered_volume) <= committed_volume` khi xác nhận.
Phần `excess_volume` được xử lý theo `excess_handling`.

### `cash_ledger`

Sổ Công Nợ Tiền & Lãi (Ledger 3) — theo dõi tiền mặt đã ứng/cọc, tiền lãi
phát sinh, và tiền hàng đã chốt giá phải trả khách.

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `ledger_id` | `BIGINT` | PK |
| `customer_id` | `BIGINT` | FK, NOT NULL, UNIQUE |
| `total_advanced` | `DECIMAL(18,2)` | NOT NULL, >= 0, tổng tiền đã ứng |
| `total_interest` | `DECIMAL(18,2)` | NOT NULL, >= 0, tổng lãi phát sinh |
| `total_locked_amount` | `DECIMAL(18,2)` | NOT NULL, >= 0, tổng tiền hàng đã chốt phải trả |
| `total_deposit_paid` | `DECIMAL(18,2)` | NOT NULL, >= 0, tổng tiền cọc đã chi |
| `net_balance` | `DECIMAL(18,2)` | NOT NULL, **tự tính** = `total_locked_amount - total_advanced - total_interest - total_deposit_paid` |
| `updated_at` | `DATETIME` | NOT NULL |

`net_balance > 0`: doanh nghiệp nợ khách. `net_balance < 0`: khách nợ doanh
nghiệp. Backend cập nhật trong transaction khi có giao dịch chốt giá, ứng
tiền, hoặc cọc.

## Quan hệ

Chi tiết quan hệ giữa các bảng được mô tả trong [erd.mmd](./erd.mmd).

### Ngiệp vụ nhập/chốt/xuất truyền thống

Khi tạo hoặc sửa `price_lock`, backend phải khóa `supplier` liên quan
trong transaction và áp dụng:

`price_lock.locked_volume <= supplier.usable_coffee_kg`

Nếu vi phạm, trả lỗi `409 PRICE_LOCK_EXCEEDED`. `price_lock.supplier_id` và `price_lock.item_id` xác định nhà cung cấp và mặt
hàng của lần chốt.

`supplier.usable_coffee_kg` và `item.available_kg` là các số dư tổng hợp được
cập nhật trong cùng transaction với nghiệp vụ nhập/chốt/xuất. Cần có tác vụ
đối soát định kỳ với các giao dịch để phát hiện sai lệch.

### Nghiệp vụ hàng gửi kho

Khi xác nhận `CONSIGNMENT_TICKET`:
- Backend tính `standard_weight_kg` từ `net_weight_kg`, `moisture_percent`,
  `impurity_percent` và các ngưỡng chuẩn.
- Cộng `standard_weight_kg` vào `CONSIGNMENT_BALANCE.total_sent_kg` (theo
  `customer_id` + `item_id`).
- Cập nhật `CONSIGNMENT_BALANCE.remaining_kg`.

Khi xác nhận `CONSIGNMENT_PRICE_LOCK`:
- Kiểm tra `locked_volume <= CONSIGNMENT_BALANCE.remaining_kg`.
- Cộng `locked_volume` vào `CONSIGNMENT_BALANCE.total_locked_kg`.
- Cập nhật `CONSIGNMENT_BALANCE.remaining_kg`.
- Nếu có `ADVANCE_PAYMENT` đang `ACTIVE`, tính `interest_days` và
  `interest_amount`, set `ADVANCE_PAYMENT.status = SETTLED`.
- Cộng `gross_amount` vào `CASH_LEDGER.total_locked_amount`.
- Cộng `interest_amount` vào `CASH_LEDGER.total_interest`.
- Tính `CASH_LEDGER.net_balance`.

### Nghiệp vụ ứng tiền (Trường hợp A)

Khi tạo `ADVANCE_PAYMENT`:
- Kiểm tra `advance_amount <= CONSIGNMENT_BALANCE.remaining_kg *
  market_price_at_advance * max_advance_percent / 100`.
- Cộng `advance_amount` vào `CASH_LEDGER.total_advanced`.

### Nghiệp vụ chốt trước (Trường hợp B)

Khi tạo `FORWARD_CONTRACT`:
- Cộng `deposit_amount` vào `CASH_LEDGER.total_deposit_paid`.

Khi xác nhận `FORWARD_CONTRACT_DELIVERY`:
- Kiểm tra `SUM(delivered_volume) <= FORWARD_CONTRACT.committed_volume`.
- Cộng `delivered_volume` vào `FORWARD_CONTRACT.delivered_volume`.
- Tính `FORWARD_CONTRACT.remaining_volume`.
- Nếu `remaining_volume <= 0`, set `FORWARD_CONTRACT.status = COMPLETED`.
- Nếu có `excess_volume > 0`, xử lý theo `excess_handling`.

### 3 Sổ cái (Ledgers) — Nguyên tắc cập nhật

| Nghiệp vụ | Sổ 1 (Hàng Gửi) | Sổ 2 (Hợp Đồng) | Sổ 3 (Tiền & Lãi) |
|---|---|---|---|
| Nhập gửi kho | `total_sent_kg` += | — | — |
| Chốt giá từ hàng gửi | `total_locked_kg` += | — | `total_locked_amount` += |
| Ứng tiền | — | — | `total_advanced` += |
| Ký hợp đồng | — | `committed_volume` set | `total_deposit_paid` += |
| Giao theo hợp đồng | — | `delivered_volume` += | — |
| Đối trừ tạm ứng | — | — | `total_interest` += |

Mỗi giao dịch chỉ cập nhật đúng sổ của nó, không trộn lẫn. Tất cả cập nhật
phải chạy trong transaction với locking appropriate.

## Mở rộng theo ERD mới

Các nhóm nghiệp vụ dưới đây đã có trong ERD và sẽ viết trong migration mới
(`V1`–`V12`, xem [roadmap.md](../planning/roadmap.md)):

- Mua: `PURCHASE_CONTRACT`, `PURCHASE_DELIVERY`.
- Bán: `SALES_CONTRACT` (3 loại), `SALES_DELIVERY`.
- Phiếu cân: `WEIGH_TICKET`, `WEIGH_TICKET_LINE`.
- Tồn kho: `WAREHOUSE`, `INVENTORY_TRANSACTION`.
- Tiền: `PAYMENT_VOUCHER`, `PAYMENT_ALLOCATION`, `DEBT_LEDGER`,
  `EXPENSE_CATEGORY`.
- Gửi kho: `CONSIGNMENT_TICKET`, `CONSIGNMENT_BALANCE`,
  `CONSIGNMENT_PRICE_LOCK`, `ADVANCE_PAYMENT`.
- Gửi kho khách: `CUSTOMER_CONSIGNMENT_TICKET`,
  `CUSTOMER_CONSIGNMENT_BALANCE`.
- Sổ vay: `LOAN_CONTRACT`, `LOAN_TRANSACTION`.
- Nâng cao: `REPORT_SCHEDULE`, `ALERT_RULE`.

ERD: [erd.mmd](./erd.mmd). Thiết kế: [design/README.md](../design/README.md).
Mẫu in: [design/print-templates/](../design/print-templates/README.md).