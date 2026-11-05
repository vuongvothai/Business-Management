# Kế hoạch triển khai chi tiết

> Roadmap tổng thể nằm tại [project-roadmap.md](./project-roadmap.md). Tài liệu
> này phân rã từng giai đoạn thành các công việc kỹ thuật thực hiện được.

## 1. Mục tiêu MVP

Xây dựng phiên bản đầu tiên cho một kho, gồm quản lý người dùng, nhà cung cấp,
mặt hàng, nhập hàng, cân, chốt giá, xuất hàng, tồn kho và chi phí.

## 2. Thứ tự triển khai

### Giai đoạn 1 — Chốt đặc tả và cơ sở dữ liệu

1. Rà soát và chốt [BRD](../requirements/BRD.md), business rules và
   [ERD](../architecture/erd.mmd).
2. Tạo migration MySQL cho `ROLE`, `USER`, `SUPPLIER`, `ITEM`, `CUSTOMER`.
3. Tạo migration cho `IMPORT_TICKET`, `IMPORT_WEIGHT`, `PRICE_LOCK`,
   `EXPORT_TICKET`, `EXPENSE`, `EXPENSE_DETAIL`, `MARKET_PRICE`.
4. Thêm khóa ngoại, unique key, check constraint và index.
5. Tạo dữ liệu mẫu cho vai trò, người dùng, mặt hàng và nhà cung cấp.

### Giai đoạn 2 — Backend Java

1. Khởi tạo Spring Boot với Web, JPA, MySQL, Validation và Flyway/Liquibase.
2. Xây dựng module master data: supplier, item, customer, user và role.
3. Xây dựng API tạo phiếu nhập và các lần cân.
4. Tính `number_of_bags` và `total_kg` từ `IMPORT_WEIGHT` ở backend.
5. Xác nhận phiếu nhập và cập nhật số dư nhà cung cấp, tồn kho mặt hàng.
6. Xây dựng API chốt giá cho một supplier/item; không cho chốt vượt số dư.
7. Xây dựng API xuất cho một item; cho phép `available_kg` âm.
8. Xây dựng API tổng hợp tồn kho và API quản lý chi phí.
9. Áp dụng transaction, khóa dòng và phân quyền ở backend.

### Giai đoạn 3 — Frontend React

1. Tạo layout và điều hướng theo vai trò.
2. Làm màn hình đăng nhập/phân quyền cơ bản.
3. Làm màn hình nhà cung cấp, mặt hàng và khách hàng.
4. Làm màn hình nhập hàng và nhập từng lần cân.
5. Làm màn hình chốt giá, xuất hàng và tồn kho.
6. Làm màn hình chi phí theo kỳ.
7. Hiển thị rõ tồn kho âm và số lượng cần bổ sung.

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