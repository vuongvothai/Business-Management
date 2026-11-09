# Lưu phiên bản baseline trước khi phát triển tiếp

## Mục đích

Giữ nguyên mã nguồn và dữ liệu đang dùng trước khi bắt đầu phiên bản nghiệp vụ
kế tiếp. Git tag lưu trạng thái mã nguồn; bản dump MySQL lưu dữ liệu. Hai việc
này độc lập và cần thực hiện riêng.

## Trạng thái đã đối chiếu

- Trước đợt cập nhật tài liệu này, repository ở nhánh `main`, working tree
  sạch tại commit `ad3f51d`.
- Các trường mở rộng phiếu nhập, bảng `VEHICLE`, tìm supplier nâng cao,
dashboard doanh thu/lợi nhuận và dữ liệu in hiện mới là kế hoạch trong docs;
chúng chưa có trong migration/API hiện hành.
- Vì vậy, commit trên là mốc mã nguồn có thể quay lại, không phải bằng chứng
cho thấy các trường mới đã được lưu vào database.

## Quy trình đề xuất

1. Xác nhận commit cần bảo toàn. Mốc đã đối chiếu trong repository này là
   `ad3f51d`. Nếu chỉ có các chỉnh sửa kế hoạch trong docs chưa commit, tag
   tường minh commit `ad3f51d` để giữ nguyên phiên bản ứng dụng; lưu các docs
   kế hoạch trong commit riêng sau đó. Nếu có thay đổi mã nguồn cần giữ, hãy
   commit chúng trước khi tạo tag.
2. Tạo annotated tag trỏ tới commit baseline, ví dụ:

   ```bash
   git tag -a v0.1.0-baseline ad3f51d -m "Baseline trước phiên bản nghiệp vụ kế tiếp"
   git push origin v0.1.0-baseline
   ```

   Nếu chưa muốn đẩy tag lên remote, giữ tag local và sao lưu repository ở nơi
   an toàn. Không di chuyển hoặc ghi đè tag sau khi đã phát hành.
3. Nếu database đang có dữ liệu cần giữ, xuất dump trước migration:

   ```bash
   mysqldump -h 127.0.0.1 -P 3307 -u root -p business_management > /secure/backup/location/business-management-baseline.sql
   ```

   Lệnh sẽ hỏi mật khẩu; không ghi mật khẩu vào lệnh, file tài liệu hoặc Git.
   Lưu dump ngoài repository, giới hạn quyền truy cập và kiểm tra có thể khôi
   phục trước khi xem là bản sao lưu hợp lệ.
4. Tạo nhánh riêng cho roadmap mới sau khi đã xác nhận tag và bản sao lưu.
   Không sửa các migration đã áp dụng; thêm migration mới và kiểm thử trên bản
   database khôi phục từ dump.

## Khôi phục

- Mã nguồn: checkout tag baseline hoặc tạo nhánh mới từ tag.
- Database: khôi phục dump vào database riêng để kiểm tra; không ghi đè môi
  trường đang dùng khi chưa xác nhận tên database và có bản sao lưu gần nhất.
- Ghi lại tag, commit, thời điểm dump và nơi lưu dump trong biên bản phát hành,
  không đưa thông tin xác thực vào biên bản.
