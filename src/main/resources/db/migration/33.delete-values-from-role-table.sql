DELETE
FROM
    blogging_platform.role
WHERE
    role_name IN ('ADMIN', 'STAFF', 'USER', 'STUDENT', 'TECH_COMPANY_REPRESENTATIVE');
