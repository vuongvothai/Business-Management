# Roadmap dự án

## Mục tiêu

Xây dựng hệ thống quản lý thu mua và tồn kho cà phê theo mô hình monolith với
React.js, Java và MySQL.

## Baseline và phạm vi trạng thái

Trạng thái trước đợt cập nhật tài liệu này được đối chiếu tại commit `ad3f51d`
trên `main`. Trước khi triển khai phiên bản kế tiếp, lưu tag Git và dump MySQL theo
[quy trình baseline](./version-baseline.md). Các trường bổ sung trong ERD là
đặc tả mục tiêu, chưa phải schema/API đã triển khai.

## Các mốc chính

| Mốc | Nội dung | Kết quả cần đạt | Trạng thái |
|---|---|---|---|
| M1 | Chốt yêu cầu và kiến trúc | BRD, business rules, ERD, API và database design thống nhất | Hoàn thành |
| M2 | Nền tảng database | Migration MySQL, khóa ngoại, index và dữ liệu mẫu | Hoàn thành |
| M3 | Backend nền tảng | Spring Boot, cấu hình MySQL, validation, transaction và phân quyền | Hoàn thành |
| M4 | Luồng nhập hàng | `IMPORT_TICKET`, `IMPORT_WEIGHT`, xác nhận nhập và cập nhật số dư | Hoàn thành |
| M5 | Luồng chốt giá | `PRICE_LOCK`, kiểm tra số dư nhà cung cấp | Hoàn thành |
| M6 | Luồng bán, giao và tồn kho | `EXPORT_TICKET`, `EXPORT_DELIVERY`, tồn kho và báo cáo tổng hợp | Hoàn thành |
| M7 | Frontend MVP | Màn hình nghiệp vụ theo vai trò | Hoàn thành |
| M8 | Hoàn tất MVP và baseline | Hoàn tất quản lý chi phí, kiểm thử, hướng dẫn và lưu baseline Git/database | Đang thực hiện |
| M9 | Dữ liệu nhập và tra cứu dùng lại | Trường nhập theo ERD, danh sách biển số dùng chung cho nhập/giao, tìm supplier theo CCCD/điện thoại | Chưa bắt đầu |
| M10 | Tổ chức lại giao diện nghiệp vụ | Khu vực **Thêm dữ liệu**, bỏ CRUD `ITEM`, tách Bán hàng và Giao hàng | Chưa bắt đầu |
| M11 | Dashboard và giá thị trường | Công thức và snapshot giá vốn đã chốt; biểu đồ theo ngày; giá hôm nay từ API có nguồn/thời điểm | Chưa bắt đầu |
| M12 | Chứng từ in và phát hành | Xem trước/tải mẫu in chốt/giao, kiểm thử hàm còn thiếu và hướng dẫn phát hành | Chưa bắt đầu |
| M13 | Hàng gửi kho | Migration 8 bảng mới, CRUD `CONSIGNMENT_TICKET`, `CONSIGNMENT_WEIGHT`, `CONSIGNMENT_BALANCE`, tính `standard_weight_kg`, API số dư | Chưa bắt đầu |
| M14 | Chốt giá từ hàng gửi & Ứng tiền | `CONSIGNMENT_PRICE_LOCK`, `ADVANCE_PAYMENT`, đối trừ tạm ứng, tính lãi theo ngày, cập nhật `CASH_LEDGER` | Chưa bắt đầu |
| M15 | Hợp đồng bao tiêu (Chốt trước) | `FORWARD_CONTRACT`, `FORWARD_CONTRACT_DELIVERY`, mapping phiếu cân, xử lý giao dư/thiếu, vi phạm hợp đồng | Chưa bắt đầu |
| M16 | 3 Sổ cái & Frontend | API tổng hợp 3 ledgers, màn hình Hàng Gửi Kho, Chốt Giá, Ứng Tiền, Hợp Đồng, Dashboard 3 sổ | Chưa bắt đầu |

## Thứ tự phụ thuộc

```text
M1 -> M2 -> M3 -> M4 -> M5 -> M6 -> M7 -> M8
```

M4, M5 và M6 dùng chung transaction, phân quyền và số dư nên không nên triển
khai frontend hoàn chỉnh trước khi các API backend tương ứng ổn định.

## Thứ tự phiên bản kế tiếp

```text
M8 -> M9 -> M10 -> M11 -> M12 -> M13 -> M14 -> M15 -> M16
```

M8 phải chốt baseline trước khi tạo migration mới. M9 hoàn tất mô hình dữ liệu
và API biển số trước khi nối các biểu mẫu ở M10. M11 chỉ bắt đầu biểu đồ sau
khi thống nhất công thức, ngày ghi nhận và nguồn giá; API cần trả rõ dữ liệu
thiếu thay vì dùng giá cũ như giá hôm nay. M12 dùng dữ liệu giao dịch đã lưu,
không tạo giao dịch mới khi in.

M13 phải hoàn thành migration 8 bảng mới trước khi backend M14-M15 có thể triển
khai. M14 (chốt giá + ứng tiền) phụ thuộc M13 (hàng gửi kho). M15 (hợp đồng bao
tiêu) độc lập với M14 nhưng cùng dùng `CONSIGNMENT_WEIGHT`. M16 tổng hợp cả 3 sổ
cái và frontend, phụ thuộc M13-M15.

## Phạm vi MVP

- Một kho.
- Ba vai trò: `ADMIN`, `WEIGHER`, `ACCOUNTANT`.
- Nhập hàng và nhiều lần cân.
- Chốt giá theo một nhà cung cấp và một mặt hàng trong mỗi phiếu.
- Chốt bán theo một mặt hàng trong mỗi phiếu; giao nhiều lần và cho phép tồn
  kho âm khi giao thực tế.
- Quản lý chi phí theo kỳ với nhiều danh mục.
- Lưu giá cà phê theo ngày trong `MARKET_PRICE`; tự động đồng bộ nguồn ngoài
  vẫn ngoài phạm vi cho tới khi chọn nguồn và cách cập nhật.

## Ngoài MVP

- Nhiều kho và chuyển kho.
- Upload ảnh lên storage thật.
- Đồng bộ giá thị trường tự động.
- Báo cáo nâng cao và quản lý công nợ.
- **Hàng gửi kho (ký gửi chờ chốt giá):** M13-M16.
- **Chốt giá từ hàng gửi kho & Ứng tiền:** M14.
- **Hợp đồng bao tiêu (chốt trước – giao sau):** M15.
- **3 Sổ cái (Ledgers) & Dashboard tổng hợp:** M16.

## Tiêu chí hoàn thành roadmap

- Tất cả mốc M1-M8 đạt trạng thái hoàn thành.
- Các luồng nhập, chốt giá, xuất và xem tồn kho chạy đúng business rules.
- Có kiểm thử cho transaction, phân quyền và các trường hợp số dư âm.
- Mọi migration mới được thử trên bản sao database; baseline có tag Git và
  dump đã kiểm tra khả năng khôi phục.
- **M13-M16 hoàn thành:** Hàng gửi kho, chốt giá từ hàng gửi, ứng tiền, hợp
  đồng bao tiêu, và 3 sổ cái hoạt động đúng với BRD mục 3.4-3.6 và mục 8.
- **3 sổ cái tách bạch:** Mỗi nghiệp vụ chỉ cập nhật đúng sổ của nó; không có
  giao dịch nào trộn lẫn giữa Sổ 1, Sổ 2, Sổ 3.
- **Trọng lượng quy chuẩn** được tính tự động và chính xác theo công thức;
  không cho phép nhập tay.
- **Lãi suất tạm ứng** tính đúng theo ngày thực tế; không làm tròn tháng.