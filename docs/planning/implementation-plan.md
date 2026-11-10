# Kế hoạch triển khai chi tiết

> Roadmap tổng thể nằm tại [project-roadmap.md](./project-roadmap.md).
> Tài liệu này phân rã từng giai đoạn thành các công việc kỹ thuật thực hiện được.

## 1. Mục tiêu MVP

Xây dựng phiên bản đầu tiên cho một kho, gồm quản lý người dùng, nhà cung cấp,
mặt hàng, nhập hàng, cân, chốt giá, xuất hàng, tồn kho và chi phí.

## 2. Thứ tự triển khai

### Giai đoạn 1 — Chốt đặc tả và cơ sở dữ liệu

1. Rà soát và chốt [BRD](../requirements/BRD.md), business rules và
   [ERD](../architecture/erd.mmd).
2. Tạo migration MySQL cho `ROLE`, `USER`, `SUPPLIER`, `ITEM`, `CUSTOMER`.
3. Tạo migration cho `IMPORT_TICKET`, `IMPORT_WEIGHT`, `PRICE_LOCK`,
   `EXPORT_TICKET`, `EXPORT_DELIVERY`, `EXPENSE`, `EXPENSE_DETAIL`,
   `MARKET_PRICE`.
4. Thêm khóa ngoại, unique key, check constraint và index.
5. Tạo dữ liệu mẫu cho vai trò, người dùng, mặt hàng và nhà cung cấp.

### Giai đoạn 2 — Backend Java

1. Khởi tạo Spring Boot với Web, JPA, MySQL, Validation và Flyway/Liquibase.
2. Xây dựng module master data: supplier, item, customer, user và role.
3. Xây dựng API tạo phiếu nhập và các lần cân.
4. Tính `number_of_bags` và `total_kg` từ `IMPORT_WEIGHT` ở backend.
5. Xác nhận phiếu nhập và cập nhật số dư nhà cung cấp, tồn kho mặt hàng.
6. Xây dựng API chốt giá cho một supplier/item; không cho chốt vượt số dư.
7. Xây dựng `EXPORT_TICKET` cho cam kết bán và `EXPORT_DELIVERY` cho các lần
   giao; chỉ lần giao xác nhận mới trừ `available_kg`.
8. Xây dựng API tổng hợp tồn kho và API quản lý chi phí.
9. Áp dụng transaction, khóa dòng và phân quyền ở backend.

### Giai đoạn 3 — Frontend React

1. Tạo layout và điều hướng theo vai trò.
2. Làm màn hình đăng nhập/phân quyền cơ bản.
3. Làm màn hình nhà cung cấp, mặt hàng và khách hàng.
4. Làm màn hình nhập hàng và nhập từng lần cân.
5. Làm màn hình chốt giá, xuất hàng và tồn kho.
6. Làm màn hình chi phí theo kỳ.
7. Hiển thị rõ tồn kho âm, khối lượng đã cam kết, đã giao và còn phải giao.

### Giai đoạn 4 — Kiểm thử và hoàn thiện

1. Viết unit test cho các quy tắc tính tổng và số dư.
2. Viết integration test cho transaction nhập, chốt và xuất.
3. Kiểm thử đồng thời khi chốt giá và xuất hàng.
4. Kiểm thử quyền `ADMIN`, `WEIGHER`, `ACCOUNTANT`.
5. Kiểm tra API theo [api-specification](../architecture/api-specification.md).
6. Cập nhật hướng dẫn phát triển và triển khai.

## 3. Tiêu chí hoàn thành MVP

- Dữ liệu nhập được tính đúng từ các lần cân.
- Không chốt giá vượt số dư nhà cung cấp.
- Xuất hàng có thể làm tồn kho âm.
- Các thay đổi số dư chạy nguyên tử trong transaction.
- Người dùng chỉ thao tác đúng quyền.
- Có test cho các luồng nghiệp vụ chính.

## 4. Kế hoạch phiên bản kế tiếp (chưa triển khai)

> Đây là kế hoạch mục tiêu, không phải mô tả schema/API hiện hành. Xem
> [version-baseline.md](./version-baseline.md) để lưu mốc trước migration mới.

### 4.1 Chốt mô hình dữ liệu

1. Thêm migration mới cho trường phiếu nhập còn thiếu theo ERD: xe tham chiếu
   và snapshot biển số, kho bốc, kho nhập, trừ bì, kết quả mẫu thử; xác định
   trường lưu kết quả tính/khấu trừ sau khi quy tắc được duyệt.
2. Tạo bảng `VEHICLE` tối giản gồm `license_plate`, `active` và audit
   timestamps. Thêm `vehicle_id` và snapshot biển số vào `IMPORT_TICKET` và
   `EXPORT_DELIVERY`, phù hợp quan hệ ERD; không xóa cứng biển số đã dùng.
3. Giữ `ITEM` làm dữ liệu nền cho cà phê nhân và giá thị trường, không đưa
   CRUD `ITEM` vào màn hình **Thêm dữ liệu**.
4. Chốt công thức khối lượng hữu dụng, cách áp dụng `tare_weight`, độ ẩm/tạp
   chất và xử lý ngoài dải trước khi thiết kế validation/service.
5. Tạo migration mới sau khi đã sao lưu và xác minh baseline; không viết lại
   migration đã chạy ở môi trường dùng chung.

### 4.2 Backend/API còn thiếu

1. Tìm supplier theo CCCD chính xác trước; nếu thiếu CCCD hoặc không tìm thấy,
   fallback sang số điện thoại đã chuẩn hóa. Trả danh sách ứng viên, không tự
   chọn khi trùng.
2. Quản lý xe đang hoạt động và thêm nhanh biển số; xác thực `vehicleId` rồi
   chụp lại biển số trên phiếu nhập/giao để lịch sử không đổi khi xe bị ẩn.
3. Mở rộng create/list/detail phiếu nhập để đọc/ghi các trường được duyệt,
   hiển thị kết quả từng lần cân và tổng lũy kế.
4. Đã chốt công thức và mốc ngày: ghi nhận theo ngày giao đã xác nhận;
   doanh thu = kg giao × giá bán/kg; lãi gộp = doanh thu trừ giá vốn
   snapshot/kg × kg giao. Snapshot `avg_cost_per_kg` khi tạo phiếu bán lấy từ
   giá vốn bình quân của các `PRICE_LOCK` đã xác nhận cùng mặt hàng; nếu thiếu
   dữ liệu hợp lệ thì để trống và không tính lãi gộp. Chi phí chưa phân bổ
   không được gọi là lãi ròng.
5. Tạo API dashboard theo `fromDate`, `toDate`, `itemId` tùy chọn; trả đủ
   ngày trong khoảng, số liệu 0 cho ngày không giao, và dữ liệu giá hôm nay
   rõ ràng gồm nguồn/thời điểm. Giá không có cho hôm nay phải trả là thiếu,
   không dùng âm thầm giá ngày cũ.
6. Xác định endpoint/quy trình nạp `MARKET_PRICE` (thủ công hoặc nguồn ngoài)
   trước khi cam kết API giá hôm nay; tự động đồng bộ là hạng mục riêng.
7. Trả dữ liệu in chỉ đọc cho phiếu chốt và từng lần giao; bảo đảm tính tiền,
   trạng thái và thông tin đối tác lấy từ bản ghi đã lưu.

Các hàm/dịch vụ cần lập kế hoạch trước khi code:

| Khu vực | Hàm dự kiến | Trách nhiệm |
|---|---|---|
| Supplier | `search(identityNo, phone)`, `normalizePhone(phone)` | Tìm CCCD ưu tiên, fallback số điện thoại; chuẩn hóa và trả ứng viên |
| Vehicle | `listActive()`, `create(licensePlate)`, `update(id, licensePlate)`, `deactivate(id)` | Quản lý biển số tái sử dụng, không xóa lịch sử |
| Import | `create(request)` mở rộng, `calculateNetWeight(...)`, `calculateDeduction(...)` | Lưu trường nhập, snapshot xe, tính khối lượng sau khi quy tắc được duyệt |
| Delivery | `create(exportId, request)` mở rộng | Lưu xe/snapshot, giữ kiểm tra khối lượng giao và cập nhật tồn kho hiện có |
| Costing | `snapshotAverageCost(itemId)` | Tính theo chính sách giá vốn được duyệt và lưu snapshot trên phiếu bán |
| Dashboard | `getDailyMetrics(fromDate, toDate, itemId)` | Tổng hợp giao đã xác nhận theo ngày; không tính lãi gộp khi thiếu snapshot giá vốn |
| Market price | `getToday(itemId)`, `upsert(itemId, priceDate, price, source)` | Đọc giá hiện hành, lưu nguồn và thời điểm ghi nhận; chưa có cơ chế đồng bộ tự động |
| Expense | `listByPeriod(from, to)`, `create(request)`, `update(id, request)`, `calculateTotal(details)` | Quản lý kỳ chi phí và kiểm tra tổng bằng tổng chi tiết |
| Print data | `getPriceLockPrintData(id)`, `getDeliveryPrintData(exportId, deliveryId)` | Chuẩn hóa dữ liệu chỉ đọc cho bản xem trước/PDF |
| Frontend print | `buildPriceLockPrint(id)`, `buildDeliveryPrint(exportId, deliveryId)` | Tạo bản xem trước/tệp in từ dữ liệu API, không ghi giao dịch |

Đây là danh sách hàm mục tiêu, chưa phải API hoặc mã nguồn đã triển khai.

### 4.3 Frontend

1. Đổi khu vực **Danh mục** thành **Thêm dữ liệu**, bỏ tab/CRUD `ITEM`; giữ
   các danh mục được phép và thêm form quản lý biển số.
2. Tạo dropdown chọn biển số đang hoạt động và thao tác thêm nhanh từ khu vực
   **Thêm dữ liệu**; dùng lại ở form nhập và giao, không yêu cầu gõ lại biển số
   trên từng phiếu.
3. Hiển thị từng lần cân kèm tổng bao/kg lũy kế sau mỗi lần; hiển thị trường
   nhập mở rộng theo BRD và phản hồi validation.
4. Tách menu/trang/form **Bán hàng** và **Giao hàng**, vẫn liên kết tới cùng
   phiếu bán và các lần giao.
5. Thêm bộ lọc khoảng ngày/mặt hàng và biểu đồ doanh thu/lãi gộp; phân biệt
   rõ số liệu thực tế với số cam kết/ước tính.
6. Hiển thị giá cà phê hôm nay từ API cùng nguồn, ngày và trạng thái thiếu dữ
   liệu.
7. Thêm bản xem trước, tải/in PDF cho chốt giá và từng lần giao; không sửa
   trạng thái giao dịch từ luồng in.

### 4.4 Kiểm thử và tài liệu

1. Test migration trên database khôi phục; validation và tính toán trường nhập
   mở rộng, gồm giá trị biên/ngoài dải đã chốt.
2. Test tra cứu CCCD, fallback điện thoại, số trùng và kết quả rỗng.
3. Test chọn/ẩn biển số, snapshot biển số và lịch sử không đổi.
4. Test snapshot giá vốn theo chính sách được duyệt; dashboard theo khoảng
   ngày, ngày không có giao dịch, dữ liệu thiếu giá vốn và giá hôm nay
   thiếu/đủ nguồn.
5. Test dữ liệu in/PDF không tạo hoặc làm thay đổi giao dịch/số dư.
6. Cập nhật API specification, database design, ERD, BRD và business rules
   trước khi bắt đầu code.