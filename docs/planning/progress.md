# Tiến độ dự án

## Baseline và lưu phiên bản

- Mốc mã nguồn trước đợt cập nhật tài liệu: `main` tại commit `ad3f51d`,
  working tree sạch tại thời điểm đối chiếu.
- Quy trình tag và dump database được mô tả trong
  [version-baseline.md](./version-baseline.md); chưa tạo tag hoặc dump trong
  thay đổi tài liệu này.
- Các trường mở rộng trong ERD chưa có trong migration/entity/request hiện
  hành. Chỉ đánh dấu hoàn thành sau khi migration, API và test đã được triển khai.

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
- [x] Kết nối các màn hình frontend với API nhập hàng, chốt giá, bán/giao hàng
  và danh mục.
- [x] Bổ sung response lỗi thống nhất cho validation, lỗi nghiệp vụ và tài nguyên
  không tồn tại.
- [x] Viết test nghiệp vụ cho nhập hàng, chốt giá và giao hàng.
- [x] Viết controller test cho các API nhập hàng, chốt giá và tồn kho.
- [x] Bổ sung đăng nhập JWT và phân quyền theo role.
- [x] Bổ sung màn hình đăng nhập frontend và tự động gửi bearer token.
- [x] Bổ sung thêm, sửa và xóa nhà cung cấp, khách hàng và mặt hàng trên frontend.
- [x] Bổ sung module `VEHICLE` và trường nhập/giao mở rộng (`vehicleId`, snapshot biển số, kho, bì, kết quả mẫu thử) (2026-11-09).
- [x] Tìm supplier theo CCCD chính xác, fallback số điện thoại chuẩn hóa, trả danh sách ứng viên (2026-11-10).
- [x] Quản lý chi phí `EXPENSE`/`EXPENSE_DETAIL` theo kỳ, tự tính tổng tiền và phân quyền (2026-11-10).
- [x] Triển khai BRULE-112 theo lựa chọn `appendix_direct` + `apply_appendix_exactly`: bảng moisture exact 1 chữ số và công thức exact `total_kg * ((percent + impurity_percent) + (number_of_bags * tare_weight))`, cập nhật test (2026-11-10).
- [ ] Tách giao diện bán/giao; thêm màn hình **Thêm dữ liệu** không có CRUD `ITEM`.
- [x] Bổ sung trường kho bốc/kho giao và snapshot trên từng lần giao, đồng bộ DTO/API/test legacy (2026-11-10).
- [x] Chuẩn hóa CCCD và số điện thoại khi cập nhật supplier, nhất quán với luồng tạo/tìm kiếm (2026-11-10).
- [x] Bổ sung unit test xác nhận snapshot xe và kho trên lần giao (2026-11-10).
- [x] Bổ sung controller test cho request giao hàng mở rộng và validation độ dài kho (2026-11-10).
- [x] Chuẩn hóa biển số xe trước khi lưu, tránh trùng do khác hoa/thường hoặc khoảng trắng (2026-11-10).
- [x] Siết phân quyền đọc danh mục/xe cho ba role và ghi rõ thao tác quản trị chỉ dành cho ADMIN (2026-11-10).
- [x] Giới hạn WEIGHER chỉ xem phiếu nhập do chính mình tạo ở cả danh sách và chi tiết (2026-11-10).
- [x] Ghi nhận người dùng đăng nhập khi WEIGHER tạo phiếu nhập, không tin `createdByUserId` từ client (2026-11-10).
- [x] Từ chối tạo/cập nhật giá thị trường có ngày trong tương lai (2026-11-10).
- [x] Kiểm tra ngày nhập/giao không vượt ngày hiện tại và ngày dự kiến giao không trước ngày bán (2026-11-10).
- [x] Kiểm soát scale số thập phân theo DECIMAL của import, price lock và export (2026-11-10).
- [x] Dashboard doanh thu/lãi gộp theo ngày và API giá cà phê hôm nay (2026-11-10).
- [x] API dữ liệu chỉ đọc cho xem trước/in chứng từ chốt giá và giao hàng (2026-11-10).
- [x] Bổ sung bộ unit/controller test cho các module backend chính (2026-11-10).
- [x] Lọc chi phí theo khoảng kỳ, hỗ trợ ngày biên và kiểm tra khoảng hợp lệ (2026-11-10).

## Bước tiếp theo

1. Xác nhận và lưu baseline Git/database trước khi áp dụng migration mới.
2. Hoàn thiện kiểm thử integration/UI còn thiếu cho các luồng chính.
3. Triển khai M9: schema/API trường nhập, xe dùng lại và tìm supplier.
4. Triển khai M10-M12: tách màn hình, dashboard/giá và chứng từ in; cập nhật
   test cùng tài liệu theo từng phần.
5. Hoàn tất quản lý chi phí MVP, hardening authentication và chuẩn bị phát hành.

## Quy ước cập nhật

Sau mỗi tính năng hoàn thành, cập nhật checkbox tương ứng, ghi ngày hoàn thành
và liên kết tới mã nguồn hoặc test liên quan.