-- user_details
SELECT *
FROM
    blogging_platform.user_details AS ud
    JOIN blogging_platform.user AS u ON ud.user_id = u.user_id;

-- user_role
SELECT *
FROM
    blogging_platform.user AS u
    JOIN blogging_platform.user_role AS ul ON u.user_id = ul.user_id
    JOIN blogging_platform.role AS r ON ul.role_id = r.role_id;

-- post
SELECT *
FROM
    blogging_platform.user AS u
    JOIN blogging_platform.post AS p ON u.user_id = p.user_id;

-- post_reaction
SELECT *
FROM
    blogging_platform.post AS p
    JOIN blogging_platform.post_reaction AS pr ON p.post_id = pr.post_id;

SELECT *
FROM
    blogging_platform.user AS u
    JOIN blogging_platform.post_reaction AS pr ON u.user_id = pr.user_id;

-- post_tag
SELECT *
FROM
    blogging_platform.post AS p
    JOIN blogging_platform.post_tag AS pt ON p.post_id = pt.post_id
    JOIN blogging_platform.tag AS t ON pt.tag_id = t.tag_id;

-- comment
SELECT *
FROM
    blogging_platform.post AS p
    JOIN blogging_platform.comment AS c ON p.post_id = c.post_id;

SELECT *
FROM
    blogging_platform.user AS u
    JOIN blogging_platform.comment AS c ON u.user_id = c.user_id;

-- comment_reaction
SELECT *
FROM
    blogging_platform.comment AS c
    JOIN blogging_platform.comment_reaction AS cr ON c.comment_id = cr.comment_id;

SELECT *
FROM
    blogging_platform.user AS u
    JOIN blogging_platform.comment_reaction AS cr ON u.user_id = cr.user_id;

-- order
SELECT *
FROM
    blogging_platform.post AS p
    JOIN blogging_platform.order AS o ON p.post_id = o.post_id;

SELECT *
FROM
    blogging_platform.user AS u
    JOIN blogging_platform.order AS o ON u.user_id = o.user_id;
