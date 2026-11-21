# Backlog MVP

## P0 � B?t bu?c

- [x] T?o project Spring Boot v� c?u h?nh MySQL.
- [x] T?o migration cho c�c b?ng MVP �? tri?n khai; c�c ph?n m? r?ng theo ERD
  c?n thi?u ��?c theo d?i ? backlog phi�n b?n k? ti?p.
- [x] T?o migration seed role v� m?t h�ng m?u.
- [x] T?o CRUD nh� cung c?p, m?t h�ng, kh�ch h�ng.
- [x] T?o phi?u nh?p v� `IMPORT_WEIGHT`.
- [x] T? t�nh s? bao v� t?ng kg t? c�c l?n c�n.
- [x] X�c nh?n phi?u nh?p v� c?p nh?t s? d�.
- [x] T?o v� x�c nh?n `PRICE_LOCK`.
- [x] T? ch?i ch?t v�?t `usable_coffee_kg`.
- [x] T?o `EXPORT_TICKET` l�m phi?u ch?t b�n/c�n c? l?p h�a ��n �i?n t?.
- [x] T?o `EXPORT_DELIVERY` �? ghi nh?n nhi?u l?n giao cho m?t phi?u.
- [x] Ch? tr? t?n kho khi x�c nh?n `EXPORT_DELIVERY`.
- [x] Cho ph�p t?n kho �m khi giao th?c t?.
- [x] T?o API `GET /api/inventory/summary`.
- [x] T?o qu?n l? `EXPENSE` v� `EXPENSE_DETAIL`, g?m CRUD theo k? v� t? t�nh t?ng ti?n (2026-11-10).
- [x] Ki?m tra ph�n quy?n ba vai tr?.

## P0 � Phi�n b?n k? ti?p

- [ ] L�u baseline m? ngu?n b?ng annotated Git tag v� dump database; x�c minh
  kh�i ph?c tr�?c migration m?i (xem [version-baseline.md](./version-baseline.md)).
- [x] B? sung migration m?i v� API cho c�c tr�?ng phi?u nh?p/giao trong ERD:
  `vehicle_id`, snapshot bi?n s?, kho b?c/kho nh?p, `tare_weight` v�
  `sample_test_result` (2026-11-09).
- [x] Qu?n l? b?ng `VEHICLE` v?i bi?n s? duy nh?t, tr?ng th�i ho?t �?ng v� audit;
  cho ph�p ch?n l?i bi?n s? ? phi?u nh?p v� phi?u giao, l�u snapshot tr�n m?i
  giao d?ch (2026-11-09).
- [x] Chốt và triển khai BRULE-112 theo `appendix_direct` + `apply_appendix_exactly`: bảng 11x10 với moisture exact 1 chữ số, công thức exact không chia 100/không đổi phép trừ bì, validation và tổng tiền (2026-11-10).
- [x] B? sung t?m supplier: �u ti�n CCCD kh?p ch�nh x�c; n?u kh�ng c� CCCD ho?c
  kh�ng t?m th?y th? d�ng s? �i?n tho?i �? chu?n h�a; tr? danh s�ch ?ng vi�n �?
  ng�?i d�ng ch?n khi c� nhi?u k?t qu? (2026-11-10).
- [ ] �?i khu v?c danh m?c th�nh **Th�m d? li?u**, b? CRUD `ITEM` kh?i UI nh�ng
  gi? `ITEM` l�m d? li?u n?n; th�m qu?n l?/ch?n nhanh bi?n s?.
- [ ] T�ch m�n h?nh v� �i?u h�?ng **B�n h�ng** (`EXPORT_TICKET`) v�
  **Giao h�ng** (`EXPORT_DELIVERY`); kh�ng �?i quy t?c t?n kho hi?n t?i.
- [x] Ch?t ch? s? dashboard: ng�y giao �? x�c nh?n, doanh thu �? giao v� l?i
  g?p theo snapshot gi� v?n; API h? tr? kho?ng ng�y v� m?t h�ng t�y ch?n
  (2026-11-10).
- [x] Ch?t v� l�u snapshot gi� v?n/kg tr�n phi?u b�n theo gi� v?n b?nh qu�n t?
  `PRICE_LOCK` �? x�c nh?n; kh�ng t�nh l?i g?p khi thi?u d? li?u (2026-11-10).
- [x] Tr? gi� th? tr�?ng h�m nay t? `MARKET_PRICE`, g?m ngu?n v� th?i �i?m c?p
  nh?t; h? tr? n?p/c?p nh?t th? c�ng, t? sinh th?i �i?m khi b? tr?ng v� kh�ng
  t? �?ng �?ng b? (2026-11-10).
- [x] B? sung API d? li?u ch? �?c cho xem tr�?c phi?u ch?t gi� v� t?ng l?n giao;
  d? li?u l?y t? b?n ghi �? l�u, s?n s�ng cho frontend t?o m?u PDF (2026-11-10).
- [x] Hi?n th? t?ng l?n c�n c�ng t?ng s? bao/kh?i l�?ng l?y k? sau t?ng l?n (2026-11-10).
- [ ] Vi?t unit/integration/UI test cho validation, tra c?u supplier, xe d�ng
  l?i, t�nh t?ng/tr? b?, dashboard, gi� thi?u, ph�n quy?n v� b?n in.

## P1 � Sau MVP

- [ ] Upload ?nh minh ch?ng l�n storage th?t.
- [ ] Audit log chi ti?t.
- [ ] �?i so�t s? d� v?i giao d?ch.
- [ ] �?ng b? gi� th? tr�?ng t? �?ng.
- [x] C?i thi?n b? l?c v� ph�n trang.

## P2 � Ch�a tri?n khai

- [ ] Nhi?u kho.
- [ ] Chuy?n kho.
- [ ] Qu?n l? c�ng n? kh�ch h�ng.
- [ ] T�ch h?p c�n �i?n t?.

## P0 - Nghiệp vụ hàng gửi kho & ứng tiền (Mới)

- [ ] **Migration M13:** Tạo 8 bảng mới: `consignment_ticket`, `consignment_weight`, `consignment_balance`, `consignment_price_lock`, `advance_payment`, `forward_contract`, `forward_contract_delivery`, `cash_ledger`; thêm FK, index, trigger audit.
- [ ] **Backend - Hàng gửi kho:**
  - [ ] CRUD `CONSIGNMENT_TICKET` với validation: tính `net_weight_kg` từ `gross_weight_kg - tare_weight_kg`, tính `standard_weight_kg` từ công thức quy chuẩn.
  - [ ] CRUD `CONSIGNMENT_WEIGHT` (nhiều lần cân, tổng hợp số bao và khối lượng).
  - [ ] Xác nhận phiếu gửi -> cập nhật `CONSIGNMENT_BALANCE` (total_sent_kg, remaining_kg) trong transaction.
  - [ ] API `GET /api/consignment/balance/{customerId}` - xem số dư hàng gửi.
  - [ ] API `POST /api/consignment/{id}/withdraw` - rút hàng, cập nhật `total_withdrawn_kg`.
- [ ] **Backend - Chốt giá từ hàng gửi:**
  - [ ] CRUD `CONSIGNMENT_PRICE_LOCK` với validation: `locked_volume <= consignment_balance.remaining_kg`, tính `gross_amount`, `net_amount`.
  - [ ] Khi xác nhận: trừ `consignment_balance`, đối trừ `advance_payment` (nếu có), cập nhật `cash_ledger`.
  - [ ] API `POST /api/consignment/price-lock` - chốt giá từ hàng gửi.
- [ ] **Backend - Ứng tiền (Trường hợp A):**
  - [ ] CRUD `ADVANCE_PAYMENT` với validation: `advance_amount <= remaining_kg * market_price * max_advance_percent / 100`.
  - [ ] Tính `interest_amount` theo ngày khi chốt giá.
  - [ ] API `POST /api/advance-payment` - tạo phiếu ứng tiền.
  - [ ] API `GET /api/advance-payment/active/{consignmentId}` - xem ứng tiền đang hoạt động.
- [ ] **Backend - Chốt trước (Trường hợp B):**
  - [ ] CRUD `FORWARD_CONTRACT` với validation: `committed_volume > 0`, `delivery_deadline >= today`.
  - [ ] CRUD `FORWARD_CONTRACT_DELIVERY` với validation: `SUM(delivered_volume) <= committed_volume`, xử lý `excess_volume`.
  - [ ] API `POST /api/forward-contract` - tạo hợp đồng bao tiêu.
  - [ ] API `POST /api/forward-contract/{id}/delivery` - giao hàng theo hợp đồng.
  - [ ] API `GET /api/forward-contract/{customerId}/progress` - xem tiến độ giao hàng.
- [ ] **Backend - 3 Sổ cái (Ledgers):**
  - [ ] API `GET /api/ledger/consignment/{customerId}` - Sổ Hàng Gửi Kho.
  - [ ] API `GET /api/ledger/forward-contract/{customerId}` - Sổ Hợp Đồng.
  - [ ] API `GET /api/ledger/cash/{customerId}` - Sổ Công Nợ Tiền & Lãi.
  - [ ] API `GET /api/ledger/all/{customerId}` - tổng hợp 3 sổ.
- [ ] **Frontend:**
  - [ ] Màn hình **Hàng Gửi Kho** - tạo phiếu gửi, xem số dư, rút hàng.
  - [ ] Màn hình **Chốt Giá Hàng Gửi** - chốt giá từ hàng gửi, xem đối trừ ứng tiền.
  - [ ] Màn hình **Ứng Tiền** - tạo phiếu ứng, xem lãi suất, theo dõi trạng thái.
  - [ ] Màn hình **Hợp Đồng Bao Tiêu** - tạo chốt trước, theo dõi tiến độ giao, xử lý giao dư/thiếu.
  - [ ] Dashboard 3 sổ cái - hiển thị song song 3 sổ cho mỗi khách.
- [ ] **Test:**
  - [ ] Unit test: tính `standard_weight_kg`, tính `interest_amount`, tính `net_amount`, tính `remaining_kg`.
  - [ ] Integration test: transaction nhập gửi -> chốt giá -> đối trừ ứng tiền.
  - [ ] Integration test: hợp đồng bao tiêu -> giao nhiều lần -> xử lý dư.
  - [ ] UI test: quy trình end-to-end hàng gửi kho.