INSERT INTO blog.user (username, password, email, created_at, updated_at)
VALUES
    ('kvossing0', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'vpenzer0@icio.us',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('gmaccook1', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'rpucker1@statcounter.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'admin@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('user', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'user@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('company', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'company@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('student', '{bcrypt}$2a$10$uvJicP4NfDVgkrpnku66i.gCpncjAvNopsGW21M1lfJbTDghGizVe', 'student@gmail.com',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO blog.user_role (user_id, role_id)
VALUES
    (1, 2),
    (2, 2),
    (3, 1),
    (4, 2),
    (5, 4),
    (6, 3);