# Hướng dẫn chạy database migration

## Trạng thái hiện tại

Repository hiện đã có migration Flyway và bộ khung Spring Boot tối thiểu,
nhưng chưa cấu hình MySQL cục bộ.

Các file migration:

- `backend/src/main/resources/db/migration/V1__create_initial_schema.sql`
- `backend/src/main/resources/db/migration/V2__insert_seed_data.sql`

Migration dùng tên vật lý `app_user` thay cho `USER` để tránh xung đột với từ
khóa/ngữ cảnh hệ thống của MySQL. Bảng này tương ứng với `USER` trong ERD.

## Việc cần chuẩn bị trên máy

1. Cài JDK phù hợp với phiên bản Spring Boot sẽ chọn.
2. Cài Maven hoặc tạo Maven Wrapper trong thư mục `backend`.
3. Cài Docker Desktop.
4. Tạo file `.env` ở thư mục gốc bằng cách sao chép `.env.example`, rồi
   thay mật khẩu local.
5. Khởi động MySQL bằng Docker:

```powershell
docker compose up -d mysql
```

Docker tự tạo database `business_management` và volume
`business_management_mysql_data`. Không ghi mật khẩu thật vào repository.
Trong cấu hình hiện tại, MySQL được mở ở cổng host `3307` vì cổng `3306`
đang được tiến trình khác sử dụng; bên trong container MySQL vẫn dùng `3306`.

## Cấu hình Spring Boot

Cấu hình đã được đặt tại
`backend/src/main/resources/application.yml`:

Đặt biến môi trường `DB_USERNAME` và `DB_PASSWORD` trước khi chạy; không ghi
mật khẩu trực tiếp vào file. Có thể nạp các giá trị từ `.env` vào PowerShell:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "mat_khau_trong_.env"
```

## Chạy và kiểm tra

Từ thư mục gốc, chạy cả MySQL và backend. Flyway sẽ tự chạy các file theo thứ
tự phiên bản:

```powershell
docker compose up -d --build
```

Kiểm tra container:

```powershell
docker compose ps
docker compose logs backend
```

Nếu chỉ muốn chạy MySQL:

```powershell
docker compose up -d mysql
```

Kiểm tra migration bằng MySQL trong container:

```powershell
docker compose exec mysql mysql -uroot -p"$env:MYSQL_ROOT_PASSWORD" business_management -e "SHOW TABLES;"
docker compose exec mysql mysql -uroot -p"$env:MYSQL_ROOT_PASSWORD" business_management -e "SELECT installed_rank, version, description, success FROM flyway_schema_history ORDER BY installed_rank;"
```

## Quy tắc thay đổi schema

Không sửa migration đã chạy. Tạo file mới, ví dụ:

```text
V3__add_customer_tax_code.sql
```

Các quy tắc tính tổng `IMPORT_WEIGHT`, kiểm tra tổng giao không vượt cam kết
và cập nhật số dư tồn kho phải nằm trong service transaction; không thể
được đảm bảo đầy đủ chỉ bằng các constraint của migration.
