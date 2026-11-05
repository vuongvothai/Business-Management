# Đặc tả API

## Quy ước chung

- Base path: `/api`
- Định dạng: `application/json`
- Ngày tháng: `YYYY-MM-DD`
- Khối lượng: kilogram (`kg`)
- Tiền tệ: VND
- Thành công: dùng mã HTTP `200` hoặc `201` tùy thao tác.
- Lỗi validation: `400`; không tìm thấy tài nguyên: `404`; xung đột nghiệp vụ:
  `409`.
- `409 PRICE_LOCK_EXCEEDED`: khối lượng chốt vượt số dư hữu dụng của nhà cung cấp.
- `409 SUPPLIER_BALANCE_EXCEEDED`: số lượng chốt vượt `supplier.usableCoffeeKg`
  nhà cung cấp.

Response lỗi thống nhất:

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Dữ liệu không hợp lệ",
  "details": [
    { "field": "numberOfBags", "message": "Phải lớn hơn 0" }
  ]
}
```

Các lỗi validation từ request body/query được ánh xạ thành `VALIDATION_ERROR`.
Lỗi nghiệp vụ dùng mã `BUSINESS_CONFLICT` với HTTP `409`, lỗi không tìm thấy
tài nguyên dùng mã `NOT_FOUND` với HTTP `404`; các lỗi request còn lại dùng
`REQUEST_ERROR`. Các lỗi này được xử lý tập trung bởi
`GlobalExceptionHandler`, vì vậy các endpoint giữ cùng một response shape.

## 1. Tạo phiếu nhập kho

### `POST /api/imports`

Tạo một phiếu nhập kho mới.

Request:

```json
{
  "supplierId": 1,
  "itemId": 2,
  "importDate": "2026-11-05",
  "weights": [
    { "weightKg": 120.3, "bagCount": 2 }
  ],
  "moisturePercent": 12.5,
  "impurityPercent": 1.8,
  "photoUrl": "https://storage.example/imports/101.jpg",
  "note": "Nhập từ kho nhà cung cấp"
}
```

Trong phiên bản hiện tại, `createdByUserId` là tùy chọn để phục vụ giai đoạn
chưa tích hợp đăng nhập. Nếu bỏ qua, backend dùng người dùng hệ thống đầu tiên
được tạo bởi migration. Khi triển khai authentication, giá trị này sẽ được lấy
từ người dùng đang đăng nhập thay vì từ request.

Quy tắc:

- `supplierId` và `itemId` phải tồn tại.
- `numberOfBags` được backend tính bằng tổng `weights[].bagCount`.
- `weightKg` của từng lần cân phải lớn hơn 0.
- `bagCount` của từng lần cân chỉ nhận `1` hoặc `2`.
- `totalKg` được backend tính bằng tổng `weights[].weightKg`.
- `moisturePercent` nằm trong khoảng `0` đến `100`.
- `impurityPercent` nằm trong khoảng `0` đến `100`, biểu thị tỷ lệ tạp chất của lô
  hàng theo phần trăm.
- Backend phải tính `totalKg` từ các lần cân trong transaction; client không
  được tự ghi đè giá trị này.

Response `201 Created`:

```json
{
  "id": 101,
  "supplierId": 1,
  "itemId": 2,
  "importDate": "2026-11-05",
  "numberOfBags": 2,
  "weights": [
    { "weightKg": 120.3, "bagCount": 2 }
  ],
  "totalKg": 120.3,
  "moisturePercent": 12.5,
  "impurityPercent": 1.8,
  "photoUrl": null,
  "note": "Nhập từ kho nhà cung cấp"
}
```

## 2. Lấy danh sách phiếu nhập

### `GET /api/imports`

Tham số query tùy chọn:

| Tham số | Kiểu | Ý nghĩa |
|---|---|---|
| `fromDate` | `date` | Ngày nhập bắt đầu |
| `toDate` | `date` | Ngày nhập kết thúc |
| `supplierId` | `integer` | Lọc theo nhà cung cấp |
| `page` | `integer` | Trang, bắt đầu từ 0 |
| `size` | `integer` | Số bản ghi mỗi trang |

Response `200 OK`:

```json
{
  "content": [
    {
      "id": 101,
      "supplier": { "id": 1, "name": "Nguyễn Văn A" },
      "item": { "id": 2, "name": "Cà phê nhân", "category": "Loại 1" },
      "importDate": "2026-11-05",
      "totalKg": 1210.0,
      "status": "CONFIRMED"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

Khi không truyền bộ lọc, API vẫn dùng `page=0`, `size=20` mặc định. `size`
được giới hạn từ 1 đến 100; ngày bắt đầu không được lớn hơn ngày kết thúc.

## 3. Chốt giá

### `POST /api/price-locks`

Chốt giá cho toàn bộ hoặc một phần khối lượng đã nhập.

Request:

```json
{
  "lockDate": "2026-11-05",
  "supplierId": 1,
  "lockedPricePerKg": 42000,
  "itemId": 2,
  "lockedVolume": 500.0,
  "note": "Chốt giá đợt 1"
}
```

Quy tắc:

- `itemId` phải tồn tại.
- `lockedVolume` phải lớn hơn 0 và không vượt `supplier.usableCoffeeKg`.
- `lockedPricePerKg` phải lớn hơn 0.
- `amount` được backend tính bằng `lockedVolume * lockedPricePerKg`.
- Request và kiểm tra/trừ `supplier.usable_coffee_kg` phải chạy trong
  cùng transaction.

API hiện tại tạo phiếu ở trạng thái `CONFIRMED`, tự tính `amount` và trừ
`supplier.usable_coffee_kg` trong cùng transaction. Nếu số dư không đủ, API
trả `409 PRICE_LOCK_EXCEEDED` và không tạo phiếu.

Response `201 Created`:

```json
{
  "id": 55,
  "supplierId": 1,
  "lockDate": "2026-11-05",
  "itemId": 2,
  "totalLockedKg": 500.0,
  "totalAmount": 21000000,
  "lockedVolume": 500.0,
  "amount": 21000000,
  "lockedPricePerKg": 42000,
  "note": "Chốt giá đợt 1"
}
```

Một số dư cà phê của nhà cung cấp có thể được chốt nhiều lần. `amount` là
giá trị tính từ `lockedVolume * lockedPricePerKg`.

## 4. Phiếu chốt bán và giao hàng

### `POST /api/exports`

Tạo `EXPORT_TICKET` để chốt bán và làm căn cứ lập hóa đơn điện tử.
Request chứa `itemId`, `committedVolume`, giá bán và ngày giao dự kiến.
Việc tạo hoặc xác nhận phiếu chưa trừ tồn kho.

### `POST /api/exports/{exportId}/deliveries`

Ghi nhận một lần giao thực tế:

```json
{
  "deliveredVolume": 3000.0,
  "deliveryDate": "2026-11-06",
  "note": "Giao lần 1"
}
```

Quy tắc:

- `deliveredVolume > 0`.
- Tổng các lần giao không bị hủy không được vượt `committedVolume`.
- Khi xác nhận giao, backend trừ `ITEM.available_kg`.
- Nếu tồn kho sau giao âm, API vẫn ghi nhận thành công.
- Response phải trả `committedVolume`, `deliveredVolume` và
  `remainingVolume`.

API hiện tại đã hỗ trợ nhiều lần giao cho cùng một phiếu. Tạo
`EXPORT_TICKET` không trừ tồn kho; chỉ `POST /api/exports/{exportId}/deliveries`
mới trừ `item.available_kg`. Khi tổng giao bằng khối lượng cam kết, trạng thái
phiếu chuyển thành `COMPLETED`; nếu chưa đủ thì là `PARTIALLY_DELIVERED`.

## 5. Chi phí theo kỳ

`EXPENSE_DETAIL.amount` lưu số tiền của từng danh mục. `EXPENSE` lưu
`totalAmount` là tổng tiền của nhiều danh mục trong một khoảng thời gian, gồm
`periodStart` và `periodEnd`, thay cho một ngày chi phí đơn lẻ. Một expense có
thể có nhiều `EXPENSE_DETAIL`.

## 6. Tổng hợp tồn kho

### `GET /api/inventory/summary`

Trả về tổng tồn kho, giá trị tồn kho và số liệu theo phân loại hàng.

Response `200 OK`:

```json
{
  "totalKg": 1210.0,
  "valuedKg": 500.0,
  "unpricedKg": 710.0,
  "totalValue": 21000000,
  "byCategory": [
    {
      "category": "Loại 1",
      "totalKg": 1210.0,
      "valuedKg": 500.0,
      "unpricedKg": 710.0,
      "inventoryValue": 21000000
    },
    { "category": "Loại 2", "totalKg": 0.0, "valuedKg": 0.0, "unpricedKg": 0.0, "inventoryValue": 0 },
    { "category": "Loại 3", "totalKg": 0.0, "valuedKg": 0.0, "unpricedKg": 0.0, "inventoryValue": 0 }
  ],
  "calculatedAt": "2026-11-05T09:59:43+07:00"
}
```

`totalValue` chỉ bao gồm khối lượng đã chốt giá. Khối lượng chưa chốt được
trả riêng qua `unpricedKg`, không tự động gán giá mặc định.

API hiện tại lấy tồn kho từ `item.available_kg` và tính giá trị phần đã chốt
theo giá bình quân gia quyền của các phiếu `PRICE_LOCK` ở trạng thái
`CONFIRMED`. Nếu tồn kho lớn hơn khối lượng đã chốt, phần chênh lệch được trả
ở `unpricedKg`.

## 7. Phân quyền người dùng

| Vai trò | Quyền |
|---|---|
| `ADMIN` | Toàn quyền |
| `WEIGHER` | Tạo `IMPORT_TICKET`, xem phiếu do mình tạo |
| `ACCOUNTANT` | Xem, chốt giá, xuất hàng |