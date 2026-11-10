# BRULE-112: Tính tổng khối lượng khấu trừ (total_deduction_amount)

> Lựa chọn áp dụng: `appendix_direct` + `apply_appendix_exactly`.

Công thức bắt buộc: `total_deduction_amount = total_kg * ((percent + impurity_percent) + (number_of_bags * tare_weight))`. Không chia 100; giữ `BigDecimal`, làm tròn 2 chữ số.

## 1. Công thức tổng quát
`total_deduction_amount` = `total_kg` * ((`percent` + `impurity_percent`) + (`number_of_bags` * `tare_weight`))


---

## 2. Quy tắc xác định giá trị `percent`
Giá trị `percent` được tra cứu tự động dựa trên độ ẩm nhập vào (`moisture_percent`). Người dùng luôn nhập chính xác độ ẩm đến **1 chữ số thập phân** (Ví dụ: 15.0, 15.1, 16.2...).

### Quy luật ánh xạ chỉ số Ma trận (1-1):
* Input phải có chính xác **1 chữ số thập phân**; null, ngoài bảng hoặc sai độ chính xác là lỗi.
* **Nếu `moisture_percent` < 15.0**: `percent` mặc định bằng `0.00`.
* **Nếu `moisture_percent` >= 15.0**:
  * **Chỉ số Hàng (Row Index)** = `floor(moisture_percent)` - 15
  * **Chỉ số Cột (Column Index)** = `Chữ số thập phân đầu tiên của moisture_percent`

---

## 3. Ma trận tra cứu dữ liệu (Moisture Table)
Mảng 2 chiều kích thước 11 hàng x 10 cột, tương ứng với dải độ ẩm từ `15.0` đến `25.0`:

```javascript
// Cột (Thập phân): .0     .1     .2     .3     .4     .5     .6     .7     .8     .9
const moistureTable = [
    [ 0.00,  0.10,  0.20,  0.37,  0.47,  0.57,  0.67,  0.77,  0.88,  0.99 ], // Hàng 0 (Ứng với 15.x)
    [ 1.13,  1.24,  1.35,  1.46,  1.57,  1.68,  1.80,  1.92,  2.04,  2.16 ], // Hàng 1 (Ứng với 16.x)
    [ 2.31,  2.44,  2.57,  2.70,  2.83,  2.96,  3.10,  3.24,  3.38,  3.52 ], // Hàng 2 (Ứng với 17.x)
    [ 3.67,  3.81,  3.96,  4.11,  4.26,  4.41,  4.56,  4.71,  4.87,  5.03 ], // Hàng 3 (Ứng với 18.x)
    [ 5.20,  5.37,  5.54,  5.71,  5.83,  6.05,  6.23,  6.41,  6.59,  6.77 ], // Hàng 4 (Ứng với 19.x)
    [ 6.95,  7.13,  7.32,  7.51,  7.71,  7.91,  8.11,  8.31,  8.51,  8.71 ], // Hàng 5 (Ứng với 20.x)
    [ 8.91,  9.12,  9.34,  9.56,  9.73, 10.00, 10.22, 10.44, 10.67, 10.90 ], // Hàng 6 (Ứng với 21.x)
    [11.13, 11.36, 11.60, 11.85, 12.10, 12.35, 12.60, 12.85, 13.10, 13.36 ], // Hàng 7 (Ứng với 22.x)
    [13.62, 13.88, 14.15, 14.43, 14.71, 14.99, 15.27, 15.55, 15.84, 16.13 ], // Hàng 8 (Ứng với 23.x)
    [16.42, 16.70, 17.01, 17.31, 17.61, 17.91, 18.22, 18.53, 18.84, 19.15 ], // Hàng 9 (Ứng với 24.x)
    [19.47,  null,  null,  null,  null,  null,  null,  null,  null,  null ]  // Hàng 10 (Mốc 25.0)
];
```

---

## 4. Ví dụ minh họa (Test Cases)

* **Ví dụ 1:** Người dùng nhập `moisture_percent = 15.0`
  * Hàng = 15 - 15 = 0
  * Cột = 0
  * Kết quả: `percent = moistureTable[0][0] = 0.00`

* **Ví dụ 2:** Người dùng nhập `moisture_percent = 15.1`
  * Hàng = 15 - 15 = 0
  * Cột = 1
  * Kết quả: `percent = moistureTable[0][1] = 0.10`

* **Ví dụ 3:** Người dùng nhập `moisture_percent = 16.1`
  * Hàng = 16 - 15 = 1
  * Cột = 1
  * Kết quả: `percent = moistureTable[1][1] = 1.24`

* **Ví dụ 4:** Người dùng nhập `moisture_percent = 25.0`
  * Hàng = 25 - 15 = 10
  * Cột = 0
  * Kết quả: `percent = moistureTable[10][0] = 19.47`
