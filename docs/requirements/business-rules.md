# Business Rules

> Bản nháp v0.1. Các quy tắc cần được kiểm tra ở backend.

## 1. Quy tắc dữ liệu

| Mã | Quy tắc |
|---|---|
| BRULE-001 | `identity_no` của nhà cung cấp có thể để trống và không bắt buộc unique. |
| BRULE-002 | `ROLE.role_name` chỉ nhận `ADMIN`, `WEIGHER`, `ACCOUNTANT`. |
| BRULE-003 | `moisture_percent` và `impurity_percent` nằm trong khoảng 0-100 nếu có giá trị. |
| BRULE-004 | Khối lượng, số bao, khối lượng chốt và xuất phải lớn hơn 0. |

## 2. Phiếu nhập

| Mã | Quy tắc |
|---|---|
| BRULE-101 | `number_of_bags = SUM(IMPORT_WEIGHT.bag_count)`. |
| BRULE-102 | `total_kg = SUM(IMPORT_WEIGHT.weight_kg)`. |
| BRULE-103 | `bag_count` chỉ nhận `1` hoặc `2`. |
| BRULE-104 | Mỗi lần cân phải có người nhập và thuộc đúng một phiếu nhập. |
| BRULE-105 | `photo_url` là tùy chọn, có thể `NULL`. |
| BRULE-106 | Phiếu nhập không được dùng để chốt/xuất nếu ở trạng thái `CANCELLED`. |

## 3. Chốt giá

| Mã | Quy tắc |
|---|---|
| BRULE-201 | Một `PRICE_LOCK` gắn với đúng một `SUPPLIER` và một `ITEM`. |
| BRULE-202 | Một nhà cung cấp có thể chốt giá nhiều lần cho cùng một mặt hàng. |
| BRULE-203 | Tổng đã chốt không được vượt `SUPPLIER.usable_coffee_kg`. |
| BRULE-204 | `locked_volume` phải lớn hơn 0 và không vượt số dư hữu dụng của nhà cung cấp. |
| BRULE-205 | `amount = locked_volume * locked_price_per_kg`. |

## 4. Xuất hàng

| Mã | Quy tắc |
|---|---|
| BRULE-301 | Mỗi `EXPORT_TICKET` phải tham chiếu đúng một `ITEM`. |
| BRULE-302 | Được phép xuất vượt `ITEM.available_kg`; sau khi hoàn tất, tồn kho có thể âm. |
| BRULE-303 | `total_volume` phải lớn hơn 0 và khớp khối lượng xuất của phiếu. |

## 5. Số dư và chi phí

| Mã | Quy tắc |
|---|---|
| BRULE-401 | Khi phiếu nhập được xác nhận, cộng khối lượng hữu dụng vào `SUPPLIER.usable_coffee_kg`. |
| BRULE-402 | Khi chốt giá, trừ `usable_coffee_kg` của nhà cung cấp. |
| BRULE-403 | Khi xuất hàng, trừ `available_kg` của `ITEM`; số dư có thể âm do cam kết giao hàng. |
| BRULE-404 | `EXPENSE.total_amount = SUM(EXPENSE_DETAIL.amount)` trong một kỳ. |
| BRULE-405 | Một kỳ chi phí không có hai dòng cùng `category_name`. |

## 6. Trạng thái

```text
IMPORT_TICKET: DRAFT -> CONFIRMED
IMPORT_TICKET: DRAFT -> CANCELLED
PRICE_LOCK:    DRAFT -> CONFIRMED
EXPORT_TICKET: DRAFT -> COMPLETED
EXPORT_TICKET: DRAFT -> CANCELLED
```

Các chuyển trạng thái và thao tác chốt/xuất phải chạy trong transaction.
Backend phải khóa `SUPPLIER` khi chốt và `ITEM` khi xuất trước khi cập nhật số
dư. Chốt giá vượt `usable_coffee_kg` vẫn trả lỗi HTTP `409`; xuất vượt tồn
không trả lỗi tồn kho và được phép ghi nhận số dư âm.