# Business-Management

Ứng dụng quản lý thu mua cà phê.

## Yêu cầu

- Docker Desktop
- Node.js 20+
- Java 17+
- Maven 3.9+ (chỉ cần nếu chạy backend ngoài Docker)

## Khởi động bằng Docker

Tạo file `.env` tại thư mục gốc:

```env
MYSQL_ROOT_PASSWORD=change-this-local-password
DB_USERNAME=root
DB_PASSWORD=change-this-local-password
```

Khởi động MySQL và backend:

```powershell
docker compose up -d --build
```

Kiểm tra trạng thái hoặc xem log:

```powershell
docker compose ps
docker compose logs -f backend
```

Backend chạy tại `http://localhost:8080`.

## Khởi động frontend

Mở một PowerShell khác:

```powershell
cd frontend
npm install
npm run dev
```

Mở `http://localhost:5173` trên trình duyệt.

Frontend đã được cấu hình proxy các request `/api` tới backend.

## Dừng dự án

```powershell
docker compose down
```

Để dừng và xóa cả dữ liệu MySQL:

```powershell
docker compose down -v
```

## Chạy backend ngoài Docker

Khi MySQL đang chạy, có thể khởi động backend trực tiếp:

```powershell
cd backend
mvn spring-boot:run
```
