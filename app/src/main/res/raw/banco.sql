CREATE TABLE IF NOT EXISTS tb_user(
id integer primary key autoincrement,
first_name TEXT,
last_name TEXT);
CREATE UNIQUE INDEX idx_tb_user on tb_user(id);

