INSERT INTO category (id, kor_name, eng_name)
VALUES (21, '책/음악/티켓', 'Culture'),
       (22, '반려동물', 'Pet'),
       (188, '잡지/무크지', null),
       (189, '기타 컬처', null),
       (190, '반려동물 의류', null),
       (191, '반려동물용품', null),
       (192, '반려동물간식', null),
       (193, '반려동물장난감', null);

INSERT INTO category_relation (parent_category_id, child_category_id, depth)
VALUES (21, 188, 1),
       (21, 189, 1),
       (22, 190, 1),
       (22, 191, 1),
       (191, 192, 2),
       (191, 193, 2);
