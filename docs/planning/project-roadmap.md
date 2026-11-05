# Roadmap dự án

## Mục tiêu

Xây dựng hệ thống quản lý thu mua và tồn kho cà phê theo mô hình monolith với
React.js, Java và MySQL.

## Các mốc chính

| Mốc | Nội dung | Kết quả cần đạt | Trạng thái |
|---|---|---|---|
| M1 | Chốt yêu cầu và kiến trúc | BRD, business rules, ERD, API và database design thống nhất | Hoàn thành |
| M2 | Nền tảng database | Migration MySQL, khóa ngoại, index và dữ liệu mẫu | Hoàn thành |
| M3 | Backend nền tảng | Spring Boot, cấu hình MySQL, validation, transaction và phân quyền | Chưa bắt đầu |
| M4 | Luồng nhập hàng | `IMPORT_TICKET`, `IMPORT_WEIGHT`, xác nhận nhập và cập nhật số dư | Hoàn thành |
| M5 | Luồng chốt giá | `PRICE_LOCK`, kiểm tra số dư nhà cung cấp | Hoàn thành |
| M6 | Luồng bán, giao và tồn kho | `EXPORT_TICKET`, `EXPORT_DELIVERY`, tồn kho và báo cáo tổng hợp | Hoàn thành |
| M7 | Frontend MVP | Màn hình nghiệp vụ theo vai trò | Chưa bắt đầu |
| M8 | Kiểm thử và phát hành | Unit test, integration test, hướng dẫn triển khai | Chưa bắt đầu |

## Thứ tự phụ thuộc

```text
M1 -> M2 -> M3 -> M4 -> M5 -> M6 -> M7 -> M8
```

M4, M5 và M6 dùng chung transaction, phân quyền và số dư nên không nên triển
khai frontend hoàn chỉnh trước khi các API backend tương ứng ổn định.

## Phạm vi MVP

- Một kho.
- Ba vai trò: `ADMIN`, `WEIGHER`, `ACCOUNTANT`.
- Nhập hàng và nhiều lần cân.
- Chốt giá theo một nhà cung cấp và một mặt hàng trong mỗi phiếu.
- Chốt bán theo một mặt hàng trong mỗi phiếu; giao nhiều lần và cho phép tồn
  kho âm khi giao thực tế.
- Quản lý chi phí theo kỳ với nhiều danh mục.

## Ngoài MVP

- Nhiều kho và chuyển kho.
- Upload ảnh lên storage thật.
- Đồng bộ giá thị trường tự động.
- Báo cáo nâng cao và quản lý công nợ.

## Tiêu chí hoàn thành roadmap

- Tất cả mốc M1-M8 đạt trạng thái hoàn thành.
- Các luồng nhập, chốt giá, xuất và xem tồn kho chạy đúng business rules.
- Có kiểm thử cho transaction, phân quyền và các trường hợp số dư âm.