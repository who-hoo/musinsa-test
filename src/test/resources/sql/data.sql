INSERT INTO category (category_id, kor_name, eng_name, parent_category_id)
VALUES (21, '책/음악/티켓', 'Culture', null),
       (22, '반려동물', 'Pet', null),
       (188, '잡지/무크지', null, 21),
       (189, '기타 컬처', null, 21),
       (190, '반려동물 의류', null, 22),
       (191, '반려동물용품', null, 22),
       (192, '반려동물간식', null, 191),
       (193, '반려동물장난감', null, 191);
