# Mẫu in phiếu

HTML độc lập, mở trực tiếp trong trình duyệt rồi bấm **In**. Không phụ thuộc
CSS/JS ngoài; dữ liệu mẫu nằm trong các phần tử `id="..."`, thay bằng dữ liệu
thật từ API khi triển khai.

| File | Phiếu |
|---|---|
| [phieu-can.html](./phieu-can.html) | Phiếu cân (mua cân) |
| [phieu-mua.html](./phieu-mua.html) | Phiếu mua hàng |
| [phieu-ban.html](./phieu-ban.html) | Phiếu bán hàng |
| [phieu-chot-gia.html](./phieu-chot-gia.html) | Phiếu chốt giá |
| [phieu-giao-hang.html](./phieu-giao-hang.html) | Phiếu giao hàng |
| [phieu-chi.html](./phieu-chi.html) | Phiếu chi |
| [phieu-thu.html](./phieu-thu.html) | Phiếu thu |
| [phieu-gui-kho.html](./phieu-gui-kho.html) | Phiếu gửi kho |
| [phieu-vay.html](./phieu-vay.html) | Khế ước vay |
| [phieu-ke-khai-thue.html](./phieu-ke-khai-thue.html) | Bảng kê khai thuế nông sản (có bộ lọc ngày/tên/mặt hàng) |

## Quy ước

- Khổ A4 dọc, phiếu nằm nửa trên (210×148mm), có đường rọc.
- Font Times New Roman, cỡ 17px.
- 2 cột trái/phải, 3 ô chữ ký: Khách hàng / Bảo vệ / Người lập.
- Chỉ in `.ticket-container`; nút và `.cut-line` ẩn trên màn hình.