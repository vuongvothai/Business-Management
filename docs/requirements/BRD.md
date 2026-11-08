# Business Requirements Document (BRD)

## 1. Tổng quan

### 1.1 Tên hệ thống

**Business-Management - Quản lý thu mua và tồn kho cà phê**

### 1.2 Mục tiêu

- Quản lý thông tin nhà cung cấp, mặt hàng và người dùng.
- Đơn giản hóa tạo phiếu nhập và cân hàng (Mặc định mỗi lần cân là 2 bao ngoài ra là 1 bao)
- Lưu ảnh minh chứng khi cần (ảnh xe chở hàng,...)
- Theo dõi khối lượng đã nhập, số cà phê hữu dụng của từng nhà cung cấp, đã
  chốt giá, đã xuất và còn tồn.
- Cho phép kế toán chốt giá cho số lượng kg và giá mà người bán (nông dân) muốn chốt (Lưu ý: hàng đã nhập kho - có trong kho)
- Hỗ trợ truy xuất phiếu nhập và phiếu xuất.
- Chuẩn hóa quyền thao tác theo vai trò.

### 1.3 Phạm vi phiên bản đầu

**Trong phạm vi:**

1. Quản lý người dùng và vai trò.
2. Quản lý nhà cung cấp và mặt hàng.
3. Tạo và tra cứu phiếu nhập/xuất.
4. Ghi nhận từng lần cân, mặc định 2 bao, có thể 1 bao.
5. Chốt giá hàng nhập.
6. Xem tổng hợp tồn kho.
7. Quản lý tồn kho tổng hợp và chi phí theo kỳ.

**Ngoài phạm vi hoặc cần xác nhận:**

- Đăng nhập, đặt lại mật khẩu.
- Upload ảnh vô dử liệu nhập hàng luôn.
- Báo cáo doanh thu/lợi nhuận chi tiết.
- Đồng bộ giá thị trường tự động.
- Quản lý 1 kho nhưng nhiều tên công ty (thực ra là 1 công ty quản lý nhưng nhập/xuất tự cân nhắt là nên xuất cho công ty nào nên để kế toán tự điều chỉnh).

### Phạm vi phiên bản kế tiếp (đã lập kế hoạch, chưa triển khai)

1. Mở rộng dữ liệu phiếu nhập: biển số xe, kho bốc, kho nhập, trừ bì và kết
   quả mẫu thử.
2. Tách màn hình **Thêm dữ liệu** khỏi nghiệp vụ; mặt hàng `ITEM` là dữ liệu
   cà phê nhân cố định, không cho nhập/sửa/xóa từ màn hình này.
3. Quản lý danh sách biển số xe tối giản để người dùng chọn nhanh khi lập
   phiếu nhập; không quản lý thêm thông tin phương tiện.
4. Tìm nhà cung cấp theo CCCD hoặc số điện thoại; nếu không có CCCD thì cho
   phép tìm bằng số điện thoại.
5. Tách màn hình chốt bán và màn hình giao hàng.
6. Hiển thị biểu đồ doanh thu/lợi nhuận theo khoảng ngày được chọn và giá cà
   phê hôm nay lấy từ API.
7. Xuất mẫu phiếu chốt giá và phiếu giao hàng ở dạng thân thiện để in.

## 2. Đối tượng sử dụng

| Đối tượng | Nhu cầu chính |
|---|---|
| Admin | Quản trị toàn bộ dữ liệu, người dùng và nghiệp vụ |
| Nhân viên cân | Nhập phiếu và thông tin cân tại chổ mua |
| Kế toán | Theo dõi, chốt giá và xuất hàng |

## 3. Quy trình nghiệp vụ cấp cao

### 3.1 Nhập hàng

1. Nhân viên cân chọn nhà cung cấp và mặt hàng.
2. Tạo phiếu nhập với nhà cung cấp, chọn hoặc thêm nhanh biển số xe, kho bốc,
   kho nhập, trừ bì,
   kết quả mẫu thử, độ ẩm, tạp chất và ngày nhập.
3. Nhập một hoặc nhiều lần cân; mỗi lần cân có `bag_count`(1 hoạc 2 nhưng mặc định là 2).
4. Có thể đính kèm ảnh minh chứng.
5. Hệ thống kiểm tính tổng số bao và tổng khối lượng ->  phiếu nhập.
6. Phiếu chuyển qua các trạng thái `DRAFT`, `CONFIRMED` hoặc
   `CANCELLED`.
7. Sau mỗi lần cân, giao diện hiển thị dòng lần cân và tổng số bao/khối lượng
   lũy kế đến lần đó; lần cuối hiển thị tổng thực tế của phiếu.

### 3.3 Tổng quan và báo cáo

1. Người dùng chọn khoảng ngày trên màn hình tổng quan.
2. Hệ thống lấy dữ liệu từ API để hiển thị biểu đồ doanh thu và lợi nhuận theo
   ngày.
3. Hệ thống hiển thị giá cà phê hiện hành trong ngày theo mặt hàng và nguồn
   giá.
4. Doanh thu/lợi nhuận thực tế của biểu đồ phải phân biệt với giá trị ước tính
   trên phiếu chốt bán.

### 3.4 In chứng từ

1. Màn hình chốt giá cho phép xem bản in mẫu trước khi in.
2. Màn hình giao hàng cho phép xem bản in mẫu của lần giao.
3. Nội dung in lấy từ API, có mã phiếu, đối tác, mặt hàng, khối lượng, đơn giá,
   thành tiền, ngày lập và người thực hiện.

### 3.2 Chốt giá

1. Kế toán chọn nhà cung cấp và mặt hàng từ số dư cà phê hữu dụng.
2. Nhập giá chốt và khối lượng chốt.
3. Hệ thống không cho chốt vượt khối lượng còn lại.
4. Hệ thống trừ số dư nhà cung cấp và lưu chi tiết chốt giá.

### 3.3 Xuất hàng

1. Kế toán tạo phiếu xuất cho khách hàng.
2. Kế toán ghi nhận khối lượng cam kết bán, giá bán và thông tin lập hóa đơn
   điện tử.
3. Kế toán có thể ghi nhận nhiều lần giao hàng cho cùng một phiếu xuất.
4. Chỉ khi xác nhận lần giao thực tế, hệ thống mới trừ tồn kho.
5. Hệ thống vẫn cho phép cam kết bán vượt tồn kho hiện tại.
6. Phiếu chuyển sang `PARTIALLY_DELIVERED` khi giao một phần và
   `COMPLETED` khi giao đủ.

## 4. Yêu cầu chức năng

| Mã | Yêu cầu |
|---|---|
| BR-001 | Hệ thống quản lý nhà cung cấp, gồm tên, mã định danh, điện thoại và địa chỉ. |
| BR-002 | Hệ thống quản lý mặt hàng như `Cà phê nhân`, phân loại như `Loại 1`. |
| BR-003 | Người dùng có vai trò `ADMIN`, `WEIGHER` hoặc `ACCOUNTANT`. |
| BR-004 | Tạo phiếu nhập với số bao, tổng kg, độ ẩm, tạp chất, ảnh tùy chọn và ghi chú. |
| BR-005 | Một phiếu nhập có nhiều bản ghi `IMPORT_WEIGHT`. |
| BR-006 | Một lần cân được ghi nhận cho 1 hoặc 2 bao. |
| BR-007 | Một lần chốt giá trừ số dư cà phê hữu dụng của nhà cung cấp, không phụ thuộc phiếu nhập cụ thể. |
| BR-008 | Một số dư nhà cung cấp có thể được chốt nhiều lần cho đến khi hết số dư. |
| BR-009 | Hiển thị tổng nhập, đã chốt, chưa chốt và tồn kho. |
| BR-010 | Phiếu xuất lưu cam kết bán; các lần giao thực tế được lưu riêng và chỉ trừ tồn kho khi xác nhận giao. |
| BR-011 | Lưu người tạo và thời điểm tạo/cập nhật cho các nghiệp vụ chính. |
| BR-012 | Nhà cung cấp được tìm theo `identity_no` hoặc `phone`; khi không có CCCD thì số điện thoại là khóa tìm kiếm thay thế. |
| BR-013 | `VEHICLE` chỉ lưu biển số xe duy nhất và trạng thái hoạt động; phiếu nhập tham chiếu biển số đã chọn và lưu thêm snapshot tại thời điểm giao dịch. |
| BR-014 | `ITEM` vẫn tồn tại để liên kết tồn kho và giá thị trường nhưng không xuất hiện trong màn hình **Thêm dữ liệu** phiên bản kế tiếp. |
| BR-015 | Biểu đồ doanh thu/lợi nhuận nhận `fromDate`, `toDate` và tùy chọn mặt hàng từ API. |
| BR-016 | Giá cà phê hôm nay được lấy từ `MARKET_PRICE` theo mặt hàng và `price_date`; không dùng giá nhập tay trên frontend làm dữ liệu chính. |
| BR-017 | Màn hình bán và màn hình giao là hai luồng giao diện riêng nhưng vẫn dùng chung `EXPORT_TICKET` và `EXPORT_DELIVERY`. |
| BR-018 | Bản in chốt giá và giao hàng là dữ liệu trình bày từ API, không tạo giao dịch mới và không thay đổi số dư. |

## 5. Yêu cầu phi chức năng

- Backend Java, frontend React.js, database MySQL.
- Giao tiếp bằng REST/JSON.
- Dữ liệu tiền và khối lượng dùng kiểu chính xác (`DECIMAL`).
- Các thao tác nhập, chốt giá và xuất phải có transaction.
- Kiểm tra quyền ở backend, không chỉ ở giao diện.
- Không để một request làm tổng chốt vượt số cà phê hữu dụng của nhà cung cấp.
- Cho phép tồn kho mặt hàng âm khi đã phát sinh cam kết xuất vượt số đang có.
- Theo dõi riêng khối lượng đã cam kết, đã giao và còn phải giao.

## 6. Tiêu chí thành công

- Nhân viên cân tạo được phiếu với dữ liệu các lần cân hợp lệ.
- Kế toán chốt đơn và xuất đơn. 
- Hệ thống từ chối chốt vượt số lượng còn hữu dụng nhưng xuất thì được vì có thể hôm đó chưa giao cho khách hàng (Là các công ty chế biến) được ngay vì thể có thể tự cam kết khi người dân có thể bán cho mình đủ số lượng đó trong 3 tới 5 ngày chẳng hạn.
- Người dùng xem được tồn kho và trạng thái phiếu rõ ràng.
- Hàng xuất được gom từ tồn kho tổng hợp theo mặt hàng; không bắt buộc gắn
  trực tiếp với một phiếu nhập nguồn.
- Hóa đơn điện tử có thể dựa trên `EXPORT_TICKET`; số lượng giao thực tế được
  đối chiếu qua các bản ghi `EXPORT_DELIVERY`.
- Biểu đồ doanh thu/lợi nhuận phiên bản kế tiếp ưu tiên số liệu giao hàng đã
  xác nhận; cách tính theo ngày lập phiếu bán hay ngày giao phải được chốt
  trước khi triển khai.
- Giao diện nhập phải cho phép thêm biển số mới và chọn lại biển số đang hoạt
  động bằng danh sách dropdown/toggle list; không yêu cầu nhập lại biển số
  trong mỗi phiếu.

## 7. Các quyết định ngoài ra: 

1. `photo_url` không bắt buộc.
2. `total_kg` do hệ thống tính từ `IMPORT_WEIGHT`.
3. Sau khi `CONFIRMED`, chỉ kế toán được sửa; nhân viên cân phải liên hệ kế
   toán.
4. Mỗi `PRICE_LOCK` áp dụng cho một mặt hàng và một nhà cung cấp.
5. Hệ thống chỉ quản lý một kho.
6. Số cà phê hữu dụng tính bằng tổng khối lượng nhập.
7. Chi phí được cập nhật trực tiếp tổng tiền
   của danh mục các chi phí con (từ ngày đến ngày)?