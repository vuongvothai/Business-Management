# Tiến độ dự án

## Trạng thái hiện tại

- [x] Xác định kiến trúc monolith.
- [x] Xác định stack React.js, Java, MySQL.
- [x] Soạn BRD và business rules.
- [x] Soạn ERD có dữ liệu mẫu minh họa.
- [x] Soạn roadmap, kế hoạch triển khai và backlog.
- [x] Soạn đặc tả API và thiết kế database ở mức tài liệu.
- [x] Khởi tạo backend Java tối thiểu và Docker Compose.
- [x] Chạy migration database trên MySQL Docker.
- [x] Tạo entity và repository cho các bảng master.
- [x] Tạo và kiểm thử API CRUD cho supplier, item và customer.
- [x] Triển khai API tạo và xem phiếu nhập hàng.
- [x] Tính tổng số bao, tổng kg và cập nhật số dư supplier/tồn kho trong transaction.
- [x] Viết test tự động cho luồng nhập hàng.
- [x] Triển khai API chốt giá và kiểm tra số dư supplier.
- [x] Triển khai phiếu chốt bán và giao hàng nhiều lần.
- [x] Chỉ trừ tồn kho khi xác nhận giao hàng.
- [x] Triển khai API tổng hợp tồn kho theo phân loại.
- [x] Bổ sung bộ lọc ngày, nhà cung cấp và phân trang cho phiếu nhập.
- [x] Khởi tạo frontend React và dashboard tổng quan tồn kho.
- [x] Viết test tự động cho API chốt giá và giao hàng.
- [ ] Khởi tạo frontend React.
- [ ] Viết test nghiệp vụ.

## Bước tiếp theo

1. Bổ sung xử lý lỗi thống nhất cho toàn bộ API.
2. Viết test controller cho các endpoint chính.
3. Hoàn thiện màn hình nhập hàng, chốt giá và giao hàng.

## Quy ước cập nhật

Sau mỗi tính năng hoàn thành, cập nhật checkbox tương ứng, ghi ngày hoàn thành
và liên kết tới mã nguồn hoặc test liên quan.