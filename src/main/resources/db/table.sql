-- User 테이블
create table user(
	id int auto_increment primary key,
    email varchar(255) not null unique,
    username varchar(255) not null,
    password varchar(255) not null,
    phone_number varchar(11) not null unique,
    social_type enum ('local', 'kakao', 'google', 'naver') default 'local' not null,
    user_type enum('freelancer', 'company') not null,
    created_at timestamp default current_timestamp
);