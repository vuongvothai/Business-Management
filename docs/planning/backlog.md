# Backlog MVP

## P0 — Bắt buộc

- [x] Tạo project Spring Boot và cấu hình MySQL.
- [x] Tạo migration cho các bảng MVP đã triển khai; các phần mở rộng theo ERD
  còn thiếu được theo dõi ở backlog phiên bản kế tiếp.
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

- [ ] Lưu baseline mã nguồn bằng annotated Git tag và dump database; xác minh
  khôi phục trước migration mới (xem [version-baseline.md](./version-baseline.md)).
- [x] Bổ sung migration mới và API cho các trường phiếu nhập/giao trong ERD:
  `vehicle_id`, snapshot biển số, kho bốc/kho nhập, `tare_weight` và
  `sample_test_result` (2026-11-09).
- [x] Quản lý bảng `VEHICLE` với biển số duy nhất, trạng thái hoạt động và audit;
  cho phép chọn lại biển số ở phiếu nhập và phiếu giao, lưu snapshot trên mỗi
  giao dịch (2026-11-09).
- [ ] Chốt quy tắc tính khối lượng sau trừ bì/độ ẩm/tạp chất, bảng khấu trừ,
  đơn vị và xử lý giá trị ngoài phạm vi trước khi viết hàm tính.
- [x] Bổ sung tìm supplier: ưu tiên CCCD khớp chính xác; nếu không có CCCD hoặc
  không tìm thấy thì dùng số điện thoại đã chuẩn hóa; trả danh sách ứng viên để
  người dùng chọn khi có nhiều kết quả (2026-11-10).
- [ ] Đổi khu vực danh mục thành **Thêm dữ liệu**, bỏ CRUD `ITEM` khỏi UI nhưng
  giữ `ITEM` làm dữ liệu nền; thêm quản lý/chọn nhanh biển số.
- [ ] Tách màn hình và điều hướng **Bán hàng** (`EXPORT_TICKET`) và
  **Giao hàng** (`EXPORT_DELIVERY`); không đổi quy tắc tồn kho hiện tại.
- [ ] Chốt chỉ số dashboard: ngày ghi nhận, doanh thu đã giao, lãi gộp và cách
  xử lý chi phí; sau đó tạo API theo khoảng ngày và mặt hàng tùy chọn.
- [ ] Chốt và lưu snapshot giá vốn/kg trên phiếu bán theo chính sách giá vốn
  được duyệt; không tính lãi gộp dựa trên trường nullable chưa được điền.
- [ ] Trả giá thị trường hôm nay từ `MARKET_PRICE`, gồm nguồn và thời điểm cập
  nhật; bảo đảm dữ liệu ghi tay cũng có thời điểm ghi nhận, xác định cách cập
  nhật và không tự động đồng bộ khi chưa chọn nguồn.
- [ ] Bổ sung xem trước và tệp/mẫu PDF in phiếu chốt giá, từng lần giao; dữ liệu
  lấy từ API và chỉ đọc.
- [ ] Hiển thị từng lần cân cùng tổng số bao/khối lượng lũy kế sau từng lần.
- [ ] Viết unit/integration/UI test cho validation, tra cứu supplier, xe dùng
  lại, tính tổng/trừ bì, dashboard, giá thiếu, phân quyền và bản in.

## P1 — Sau MVP

- [ ] Upload ảnh minh chứng lên storage thật.
- [ ] Audit log chi tiết.
- [ ] Đối soát số dư với giao dịch.
- [ ] Đồng bộ giá thị trường tự động.
- [x] Cải thiện bộ lọc và phân trang.

## P2 — Chưa triển khai

- [ ] Nhiều kho.
- [ ] Chuyển kho.
- [ ] Quản lý công nợ khách hàng.
- [ ] Tích hợp cân điện tử.