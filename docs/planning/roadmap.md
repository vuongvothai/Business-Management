# Kế hoạch tổng thể

> Chưa phát hành → **sửa schema 1 lần** rồi thiết kế lại BE + FE.
> Không cần giữ tương thích ngược.

## 1. Database — viết lại migration

Xóa toàn bộ `V1`–`V13`, viết lại từ `V1` theo ERD chuẩn
([erd.mmd](../architecture/erd.mmd)).

| # | Nội dung |
|---|---|
| V1 | Danh mục: `role`, `app_user`, `warehouse`, `item`, `vehicle`, `supplier`, `customer` |
| V2 | Cân: `weigh_ticket`, `weigh_ticket_line` |
| V3 | Mua: `purchase_contract`, `purchase_delivery`, `import_ticket` |
| V4 | Gửi kho: `consignment_ticket`, `consignment_balance`, `consignment_price_lock`, `advance_payment` |
| V5 | Bán: `sales_contract`, `sales_delivery` |
| V6 | Tồn kho: `inventory_transaction` |
| V7 | Tiền: `payment_voucher`, `payment_allocation`, `expense_category`, `expense`, `expense_detail`, `debt_ledger` |
| V8 | Gửi kho khách: `customer_consignment_ticket`, `customer_consignment_balance` |
| V9 | Sổ vay: `loan_contract`, `loan_transaction` |
| V10 | Nâng cao: `report_schedule`, `alert_rule` |
| V11 | Audit: `audit_log` + trigger |
| V12 | Seed: role, admin, item, warehouse |

## 2. Backend — thiết kế lại

| # | Module | Nội dung |
|---|---|---|
| B1 | `master` | role, user, warehouse, item, vehicle, supplier, customer |
| B2 | `weighticket` | phiếu cân, dòng cân, tính quy chuẩn |
| B3 | `purchase` | hợp đồng mua, phiếu nhập, giao theo hợp đồng |
| B4 | `consignment` | gửi kho, số dư, chốt giá gửi, ứng tiền |
| B5 | `sales` | hợp đồng bán 3 loại, giao hàng |
| B6 | `inventory` | `INVENTORY_TRANSACTION`, tồn NXT |
| B7 | `payment` | thu/chi, cấn trừ, công nợ |
| B8 | `expense` | danh mục + chi phí |
| B9 | `loan` | sổ vay |
| B10 | `report` | e-Report, cảnh báo, bảng kê |

## 3. Frontend — 7 danh mục

| # | Tên |
|---|---|
| 1 | Tổng quan (doanh thu, lợi nhuận, tồn kho, phiếu nhập/xuất, biểu đồ, hoạt động gần đây) |
| 2 | Cân hàng (IN/OUT) |
| 3 | Nhập hàng (Thẳng / Chốt / Từ hàng gửi) |
| 4 | Xuất hàng (Thẳng / Chốt / Từ hàng gửi) |
| 5 | Kho (tồn + chi tiết lô) |
| 6 | Vay (đi vay / cho vay) |
| 7 | Báo cáo (doanh thu, NXT, công nợ, vay, hiệu quả) |
| 8 | Thêm dữ liệu |

Chi tiết + in phiếu: [design/frontend.md](../design/frontend.md).

## 4. Thứ tự

```text
V1–V12 → B1 → F1
V2–V3  → B2,B3 → F2 (Mua)
V4–V5  → B4,B5 → F2 (Bán), F5 (Gửi kho)
V6     → B6 → F4 (Kho)
V7     → B7,B8 → F4 (Tiền, Chi phí)
V8–V10 → B9,B10 → F5,F6 (Vay, Báo cáo)
```

## 5. Test

- [ ] Unit: quy chuẩn, lãi, số dư, tổng tiền
- [ ] Integration: nhập→chốt→giao→thu/chi; gửi kho→chốt→đối trừ
- [ ] UI: 6 luồng Mua/Bán, phân quyền 3 vai trò