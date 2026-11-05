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
- [ ] Tạo `EXPORT_TICKET` làm phiếu chốt bán/căn cứ lập hóa đơn điện tử.
- [ ] Tạo `EXPORT_DELIVERY` để ghi nhận nhiều lần giao cho một phiếu.
- [ ] Chỉ trừ tồn kho khi xác nhận `EXPORT_DELIVERY`.
- [ ] Cho phép tồn kho âm khi giao thực tế.
- [ ] Tạo API `GET /api/inventory/summary`.
- [ ] Tạo quản lý `EXPENSE` và `EXPENSE_DETAIL`.
- [ ] Kiểm tra phân quyền ba vai trò.

## P1 — Sau MVP

- [ ] Upload ảnh minh chứng lên storage thật.
- [ ] Dashboard và báo cáo doanh thu/lợi nhuận.
- [ ] Audit log chi tiết.
- [ ] Đối soát số dư với giao dịch.
- [ ] Đồng bộ giá thị trường tự động.
- [ ] Cải thiện bộ lọc và phân trang.

## P2 — Chưa triển khai

- [ ] Nhiều kho.
- [ ] Chuyển kho.
- [ ] Quản lý công nợ khách hàng.
- [ ] Tích hợp cân điện tử.