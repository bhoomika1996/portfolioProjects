-- data.sql_v1
-- Sample admin user for backend testing
INSERT INTO users (name, email, password_hash, role) VALUES (
  'Admin User',
  'admin@tracker.com',
  '$2a$10$RV3M34MG4F7PcNZQ7fCgXuwmrI4e7cxEggnT5vwLqiZnWxS9ANbKe', -- BCrypt for "adminpass"
  'ADMIN'
);