# Backlog MVP

## P0 — Bắt buộc

- [x] Tạo project Spring Boot và cấu hình MySQL.
- [x] Tạo migration cho toàn bộ bảng theo ERD.
- [x] Tạo migration seed role và mặt hàng mẫu.
- [x] Tạo CRUD nhà cung cấp, mặt hàng, khách hàng.
- [x] Tạo phiếu nhập và `IMPORT_WEIGHT`.
- [x] Tự tính số bao và tổng kg từ các lần cân.
- [x] Xác nhận phiếu nhập và cập nhật số dư.
- [x] Tạo và xác nhận `PRICE_LOCK`.
- [x] Từ chối chốt vượt `usable_coffee_kg`.
- [x] Tạo `EXPORT_TICKET` làm phiếu chốt bán/căn cứ lập hóa đơn điện tử.
- [x] Tạo `EXPORT_DELIVERY` để ghi nhận nhiều lần giao cho một phiếu.
- [x] Chỉ trừ tồn kho khi xác nhận `EXPORT_DELIVERY`.
- [x] Cho phép tồn kho âm khi giao thực tế.
- [x] Tạo API `GET /api/inventory/summary`.
- [ ] Tạo quản lý `EXPENSE` và `EXPENSE_DETAIL`.
- [x] Kiểm tra phân quyền ba vai trò.

## P0 — Phiên bản kế tiếp

- [ ] Cập nhật backend và migration cho các trường phiếu nhập: biển số xe, kho
  bốc, kho nhập, trừ bì và kết quả mẫu thử.
- [ ] Tạo bảng `VEHICLE` chỉ gồm biển số, trạng thái hoạt động và audit
  timestamps; cho phép chọn lại biển số khi lập phiếu nhập.
- [ ] Bổ sung tìm kiếm nhà cung cấp theo CCCD và số điện thoại với cơ chế thay
  thế khi thiếu CCCD.
- [ ] Đổi màn hình danh mục thành **Thêm dữ liệu**, bỏ thao tác nhập `ITEM`
  khỏi giao diện nhưng vẫn giữ `ITEM` làm dữ liệu nền.
- [ ] Tách màn hình bán và giao, giữ nguyên API/nghiệp vụ `EXPORT_TICKET` và
  `EXPORT_DELIVERY`.
- [ ] Thiết kế API tổng quan theo khoảng ngày cho doanh thu, lợi nhuận và giá
  cà phê hôm nay.
- [ ] Xác định công thức doanh thu/lợi nhuận theo số giao đã xác nhận và ngày
  dùng để vẽ biểu đồ.
- [ ] Thêm bản xem trước và xuất mẫu in cho phiếu chốt giá và phiếu giao hàng.
- [ ] Viết test cho tìm kiếm nhà cung cấp, dữ liệu nhập mở rộng, dashboard và
  bản in.
- [ ] Hiển thị từng lần cân và tổng lũy kế sau mỗi lần cân trên màn hình nhập.

## P1 — Sau MVP

- [ ] Upload ảnh minh chứng lên storage thật.
- [ ] Dashboard và báo cáo doanh thu/lợi nhuận.
- [ ] Audit log chi tiết.
- [ ] Đối soát số dư với giao dịch.
- [ ] Đồng bộ giá thị trường tự động.
- [x] Cải thiện bộ lọc và phân trang.

## P2 — Chưa triển khai

- [ ] Nhiều kho.
- [ ] Chuyển kho.
- [ ] Quản lý công nợ khách hàng.
- [ ] Tích hợp cân điện tử.