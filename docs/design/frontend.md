# Frontend — 7 danh mục

## Danh mục

| # | Tên | Nội dung | Bộ lọc |
|---|---|---|---|
| 1 | Tổng quan | Doanh thu, lợi nhuận, tồn kho, số phiếu nhập/xuất; biểu đồ nhập–xuất theo tấn; hoạt động gần đây | Tháng |
| 2 | Cân hàng | Nhận dữ liệu app trạm cân / nhập tay; chọn Cân nhập / Cân xuất | Ngày, chiều, trạng thái |
| 3 | Nhập hàng | 3 loại: Thẳng / Chốt / Từ hàng gửi | Ngày, NCC, mặt hàng, loại, trạng thái |
| 4 | Xuất hàng | 3 loại: Thẳng / Chốt / Từ hàng gửi | Ngày, khách, mặt hàng, loại, trạng thái |
| 5 | Kho | Tồn theo mặt hàng; chi tiết nhập ngày nào, của ai, thông số | Kho, mặt hàng |
| 6 | Vay | Đi vay / Cho vay | Trạng thái, đối tượng |
| 7 | Báo cáo | Doanh thu–lợi nhuận, NXT, công nợ, vay, hiệu quả thu mua | Ngày, mặt hàng, kho |
| 8 | Thêm dữ liệu | Khách, NCC, số xe, nhân viên | — |

## In phiếu

Nút **In** đặt trên từng màn hình, không có menu riêng.

| Phiếu | Màn hình |
|---|---|
| Phiếu cân | Cân hàng |
| Phiếu mua / chốt giá | Nhập hàng |
| Phiếu bán / giao | Xuất hàng |
| Phiếu chi / thu | Báo cáo → Công nợ |
| Phiếu gửi kho | Kho |
| Khế ước vay | Vay |
| Kê khai thuế | Báo cáo → Thuế |

Bộ lọc in = bộ lọc màn hình. Kê khai thuế bắt buộc chọn kỳ.

## API

| Danh mục | API |
|---|---|
| Tổng quan | `/api/master/items`, `/api/inventory/transactions` |
| Cân hàng | `/api/weigh-tickets` |
| Nhập hàng | `/api/purchase/*` |
| Xuất hàng | `/api/sales/*` |
| Kho | `/api/inventory/*`, `/api/consignment/balances` |
| Vay | `/api/loan/*` |
| Báo cáo | `/api/payment/*`, `/api/expense/*`, `/api/report/*` |
| Thêm dữ liệu | `/api/master/*` |