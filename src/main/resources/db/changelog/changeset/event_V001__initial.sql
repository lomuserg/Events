CREATE TABLE if not exists "user" (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE, -- Автоинкрементный первичный ключ
    username VARCHAR(64) NOT NULL UNIQUE, -- Уникальное имя пользователя
    email VARCHAR(64) NOT NULL UNIQUE, -- Уникальный email
    phone VARCHAR(32) UNIQUE, -- Уникальный телефон
    password VARCHAR(128) NOT NULL, -- Пароль
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Дата создания
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Дата обновления
    banned BOOLEAN NOT NULL DEFAULT FALSE -- Статус блокировки
);