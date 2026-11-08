# Thiết kế cơ sở dữ liệu

## Nguyên tắc

- Sử dụng MySQL.
- Khóa chính dùng số nguyên tự tăng.
- Khối lượng, đơn giá và thành tiền dùng `DECIMAL`, không dùng `FLOAT`.
- Tên cột trong database dùng `snake_case`; API chuyển sang `camelCase`.
- Tạo index cho các khóa ngoại và các cột thường dùng để lọc: `import_date`,
  `supplier_id`, `item_id`.
- Các bảng giao dịch chính lưu `created_at`, `created_by`, `updated_at` và
  `updated_by` để phục vụ audit.

## Bảng phục vụ API hiện tại

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

`vehicle` chỉ lưu danh sách biển số xe dùng lại trong màn hình nhập hàng; không
quản lý chủ xe, loại xe hoặc thông tin vận tải khác.

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
| `vehicle_id` | `BIGINT` | FK, NULL |
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
| `committed_volume` | `DECIMAL(14,3)` | NOT NULL, > 0 |
`delivery_due_date` không còn thuộc mô hình hiện tại; ngày giao thực tế nằm ở
`export_delivery.delivery_date`.

`export_delivery` lưu từng lần giao hàng thực tế:

| Cột | Kiểu | Ràng buộc |
|---|---|---|
| `delivery_id` | `BIGINT` | PK |
| `export_id` | `BIGINT` | FK, NOT NULL |
| `delivered_volume` | `DECIMAL(14,3)` | NOT NULL, > 0 |
| `delivery_date` | `DATE` | NOT NULL |
| `status` | `VARCHAR(20)` | NOT NULL: `CONFIRMED`, `CANCELLED` |
| `created_by` | `BIGINT` | FK, NOT NULL |
| `created_at` | `DATETIME` | NOT NULL |

`delivered_volume` được tính bằng tổng các lần giao không bị hủy.
`remaining_volume = committed_volume - delivered_volume`. Khi xác nhận một
`EXPORT_DELIVERY`, backend khóa `ITEM` trong transaction và trừ
`delivered_volume` khỏi `ITEM.available_kg`. Tạo phiếu cam kết không làm thay
đổi tồn kho. Cam kết bán có thể vượt tồn hiện tại.

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
một ngày.


## Quan hệ

Chi tiết quan hệ giữa các bảng được mô tả trong [erd.mmd](./erd.mmd).

Khi tạo hoặc sửa `price_lock`, backend phải khóa `supplier` liên quan
trong transaction và áp dụng:

`price_lock.locked_volume <= supplier.usable_coffee_kg`

Nếu vi phạm, trả lỗi `409 PRICE_LOCK_EXCEEDED`. `price_lock.supplier_id` và `price_lock.item_id` xác định nhà cung cấp và mặt
hàng của lần chốt.

`supplier.usable_coffee_kg` và `item.available_kg` là các số dư tổng hợp được
cập nhật trong cùng transaction với nghiệp vụ nhập/chốt/xuất. Cần có tác vụ
đối soát định kỳ với các giao dịch để phát hiện sai lệch.