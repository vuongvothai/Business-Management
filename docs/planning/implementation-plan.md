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

### 4.1 Chốt mô hình dữ liệu

1. Giữ các trường mở rộng hiện có trên `IMPORT_TICKET`: biển số xe, kho bốc,
   kho nhập, trừ bì và kết quả mẫu thử.
2. Tạo bảng `VEHICLE` tối giản chỉ gồm `license_plate`, `active` và audit
   timestamps; thêm `vehicle_id` vào `IMPORT_TICKET` và giữ snapshot biển số.
3. Giữ `ITEM` làm dữ liệu nền cho cà phê nhân và giá thị trường, không đưa
   CRUD `ITEM` vào màn hình **Thêm dữ liệu**.
4. Chốt cách tính khối lượng hữu dụng sau khi áp dụng `tare_weight`.

### 4.2 Backend/API còn thiếu

1. Bổ sung query tìm supplier theo `identity_no` và `phone`, có fallback khi
   CCCD trống.
2. Bổ sung API quản lý biển số xe và API chọn biển số khi tạo phiếu nhập.
3. Bổ sung API dashboard theo `fromDate`, `toDate`, `itemId` tùy chọn, gồm
   chuỗi doanh thu/lợi nhuận theo ngày và giá cà phê hiện hành.
4. Chốt công thức dùng giao hàng đã xác nhận cho số liệu thực tế; tách rõ
   doanh thu/lợi nhuận ước tính trên `EXPORT_TICKET`.
5. Bổ sung endpoint dữ liệu in cho phiếu chốt giá và phiếu giao hàng, ưu tiên
   response ổn định để frontend tạo bản xem trước/in.

Các hàm/dịch vụ cần lập kế hoạch trước khi code:

| Khu vực | Hàm dự kiến | Trách nhiệm |
|---|---|---|
| Supplier | `search(identityNo, phone)` | Tìm theo CCCD, fallback sang số điện thoại |
| Vehicle | `listActive()`, `create()`, `update()`, `deactivate()` | Quản lý biển số dùng lại, không xóa lịch sử |
| Import | `create()` / `update()` mở rộng | Nhận trường xe, kho, trừ bì và mẫu thử |
| Dashboard | `getDailyMetrics(fromDate, toDate, itemId)` | Doanh thu/lợi nhuận theo ngày |
| Market price | `getToday(itemId)` | Lấy giá hiện hành từ `MARKET_PRICE` |
| Print data | `getPriceLockPrintData(id)`, `getDeliveryPrintData(exportId, deliveryId)` | Chuẩn hóa dữ liệu bản in |

Đây là danh sách hàm mục tiêu, chưa phải API hoặc mã nguồn đã triển khai.

### 4.3 Frontend

1. Đổi tên khu vực danh mục thành **Thêm dữ liệu**.
2. Bỏ tab nhập mặt hàng; thêm dropdown/toggle list biển số và thao tác thêm
   biển số mới.
3. Hiển thị từng lần cân kèm tổng số bao/khối lượng lũy kế sau từng lần cân.
4. Tách màn hình **Bán hàng** và **Giao hàng**, giữ chung dữ liệu giao dịch.
5. Thêm bộ lọc ngày và biểu đồ doanh thu/lợi nhuận trên màn hình tổng quan.
6. Hiển thị giá cà phê hôm nay theo dữ liệu API.
7. Thêm bản xem trước và nút in cho chốt giá và giao hàng.

### 4.4 Kiểm thử và tài liệu

1. Test validation cho trường nhập mở rộng và `tare_weight`.
2. Test tìm supplier bằng CCCD, số điện thoại và trường hợp thiếu CCCD.
3. Test dashboard theo khoảng ngày, công thức lợi nhuận và giá hiện hành.
4. Test dữ liệu in không làm thay đổi giao dịch hoặc số dư.
5. Cập nhật API specification, database design và BRD trước khi bắt đầu code.