# User Roles and Permissions

> Bản nháp v0.1. Quyền phải được kiểm tra ở backend theo `ROLE.role_name`.

## 1. Vai trò

| Role | Tên hiển thị | Quyền chính |
|---|---|---|
| `ADMIN` | Quản trị viên | Toàn quyền |
| `WEIGHER` | Nhân viên cân | Tạo phiếu nhập, xem phiếu do mình tạo |
| `ACCOUNTANT` | Kế toán | Xem, chốt giá, xuất hàng |

## 2. Ma trận quyền

| Chức năng | ADMIN | WEIGHER | ACCOUNTANT |
|---|---:|---:|---:|
| Xem dữ liệu tổng quan | Có | Có | Có |
| Quản lý người dùng/vai trò | Có | Không | Không |
| Quản lý nhà cung cấp | Có | `[CẦN XÁC NHẬN]` | `[CẦN XÁC NHẬN]` |
| Quản lý mặt hàng | Có | Xem | Xem |
| Tạo `IMPORT_TICKET` | Có | Có | `[CẦN XÁC NHẬN]` |
| Xem mọi phiếu nhập | Có | Không | Có |
| Xem phiếu do mình tạo | Có | Có | Có |
| Sửa phiếu `DRAFT` | Có | Có, phiếu của mình | Có |
| Sửa phiếu `CONFIRMED` | Có | Không | Có |
| Xác nhận phiếu nhập | Có | `[CẦN XÁC NHẬN]` | `[CẦN XÁC NHẬN]` |
| Chốt giá | Có | Không | Có |
| Tạo/hoàn tất phiếu xuất | Có | Không | Có |
| Hủy chứng từ | Có | `[CẦN XÁC NHẬN]` | `[CẦN XÁC NHẬN]` |
| Xem báo cáo | Có | `[CẦN XÁC NHẬN]` | Có |

## 3. Phạm vi dữ liệu

- `WEIGHER` chỉ xem phiếu nhập có `created_by` là chính mình, trừ khi được
  cấp quyền bổ sung.
- `ACCOUNTANT` xem dữ liệu cần cho chốt giá, tồn kho và xuất hàng.
- `ADMIN` không bị giới hạn phạm vi dữ liệu.
- Mọi thao tác tạo, sửa, xác nhận hoặc hủy phải ghi nhận người thực hiện.

## 4. Quyền API đề xuất

| API | Quyền tối thiểu |
|---|---|
| `POST /api/imports` | `ADMIN`, `WEIGHER` |
| `GET /api/imports` | Theo phạm vi dữ liệu của role |
| `POST /api/price-locks` | `ADMIN`, `ACCOUNTANT` |
| `GET /api/inventory/summary` | `ADMIN`, `ACCOUNTANT` `[CẦN XÁC NHẬN]` |
| `POST /api/exports` | `ADMIN`, `ACCOUNTANT` |

## 5. Câu hỏi cần xác nhận

1. Nhân viên cân có được xem tồn kho tổng không?
2. Ai được chuyển phiếu từ `DRAFT` sang `CONFIRMED`?
3. Kế toán được sửa phiếu đã xác nhận theo quyết định trong BRD.
4. Có cần thêm vai trò quản lý kho/người duyệt không?