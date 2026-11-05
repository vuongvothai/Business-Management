INSERT INTO app_user (full_name, role_id)
SELECT 'System administrator', role_id
FROM role
WHERE role_name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM app_user WHERE full_name = 'System administrator'
  );
