# Mở rộng: Thu mua – Bán – Kho – Tiền

> ERD chuẩn: [erd.mmd](../architecture/erd.mmd). Kế hoạch:
> [planning/roadmap.md](../planning/roadmap.md).

## 1. Nguyên tắc

- Giữ `V1`–`V13`, `BRULE-001`…`BRULE-503`.
- Mua/bán dùng chung phiếu cân, tồn kho, tiền. Khác ở `direction`/`contract_type`.
- Số dư là cache; đối soát với `INVENTORY_TRANSACTION`, `PAYMENT_ALLOCATION`.
- Cập nhật số dư trong transaction, khóa bản ghi.

## 2. Bán 3 loại

`SALES_CONTRACT.contract_type`: `DIRECT` (xuất ngay), `LOCKED` (giao sau),
`FROM_CONSIGNMENT` (trừ gửi kho khách — `CUSTOMER_CONSIGNMENT_*`, ERD mục 9).

## 3. Phiếu cân

`WEIGH_TICKET` + `WEIGH_TICKET_LINE`. `net = gross − tare`;
`standard = net × (1 − max(0, ẩm − ẩm_std)/100) × (1 − max(0, tạp − tạp_std)/100)`.

## 4. Tồn kho

`INVENTORY_TRANSACTION` (`IN`/`OUT`/`ADJUST`), `ITEM.actual_stock_kg`,
`ITEM.standard_stock_kg`.

## 5. Tiền

`PAYMENT_VOUCHER` (`RECEIPT`/`PAYMENT`), `PAYMENT_ALLOCATION` cấn trừ.
Công nợ tính từ voucher + allocation (không có `DEBT_LEDGER`).

## 6. Chi phí

`EXPENSE_CATEGORY` → `EXPENSE` → `EXPENSE_DETAIL`.

## 7. Đã có trong ERD

`DEBT_LEDGER`, `CUSTOMER_CONSIGNMENT_*`, `LOAN_*`, `REPORT_SCHEDULE`,
`ALERT_RULE` — xem ERD mục 8–11.

## 8. Migration

Theo [planning/roadmap.md](../planning/roadmap.md) mục 1 — viết lại `V1`–`V12`.

## 9. BRULE mới

`BRULE-601`…`BRULE-614` — [business-rules.md](../requirements/business-rules.md) mục 8.

## 10. Mẫu in

[print-templates/](./print-templates/README.md).

## 11. Đã chốt

1. `SUPPLIER.usable_coffee_kg`: **giữ**.
2. `USER`: **thêm** `username` + `password_hash`.
3. `PURCHASE_DELIVERY.import_id`: **1-1**.
4. `ITEM`: **actual + standard** (2 cột).
5. `EXPENSE_DETAIL`: **category_id** FK.
6. Thêm vào ERD: **có** — `DEBT_LEDGER`, `CUSTOMER_CONSIGNMENT_*`, `LOAN_*`,
   `REPORT_SCHEDULE`, `ALERT_RULE`.