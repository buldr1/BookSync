CREATE DATABASE IF NOT EXISTS booksync;
USE booksync;

-- Create Genres Table
CREATE TABLE IF NOT EXISTS genres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL
);

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    sex VARCHAR(10),
    admin BOOLEAN NOT NULL,
    login VARCHAR(20) NOT NULL,
    password VARCHAR(64) NOT NULL,
    genre_id INT,
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- Create Books Table
CREATE TABLE IF NOT EXISTS books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    publisher VARCHAR(255),
    qtd_reviews INT,
    review_score DECIMAL(3,2),
    public BOOLEAN NOT NULL,
    cover MEDIUMBLOB,
    synopsis VARCHAR(2500),
    genre_id INT,
    CONSTRAINT fk_genre_id_books FOREIGN KEY (genre_id) REFERENCES genres(id)
);

CREATE TABLE book_reviews (
    review_id SERIAL PRIMARY KEY,
    book_id INT,
    user_id INT,
    book_review VARCHAR(2500),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create N to N Relationship Tables (users_genres and books_genres)
CREATE TABLE IF NOT EXISTS users_genres (
    user_id INT,
    genre_id INT,
    PRIMARY KEY (user_id, genre_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_genre_id_ug FOREIGN KEY (genre_id) REFERENCES genres(id)
);

CREATE TABLE IF NOT EXISTS books_genres (
    book_id INT,
    genre_id INT,
    PRIMARY KEY (book_id, genre_id),
    CONSTRAINT fk_book_id FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT fk_genre_id_bg FOREIGN KEY (genre_id) REFERENCES genres(id)
);

-- Genres Table Inserts
INSERT INTO genres (name) VALUES ('Ficção');
INSERT INTO genres (name) VALUES ('Mistério');
INSERT INTO genres (name) VALUES ('Ficção Científica');
INSERT INTO genres (name) VALUES ('Romance');
INSERT INTO genres (name) VALUES ('Suspense');

-- Users Table Inserts
INSERT INTO users (name, sex, admin, login, password, genre_id) VALUES ('John Doe', 'Masculino', true, 'john_doe', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 1);-- password123
INSERT INTO users (name, sex, admin, login, password, genre_id) VALUES ('Jane Smith', 'Feminino', false, 'jane_smith', '1d4598d1949b47f7f211134b639ec32238ce73086a83c2f745713b3f12f817e5', 2);-- pass456
INSERT INTO users (name, sex, admin, login, password, genre_id) VALUES ('Bob Johnson', 'Masculino', false, 'bob_johnson', '68256910bc07ec7457bcb7f4374e43ab5c98eda8d8eb8e1e53d6e33a47e8afe3', 3);-- securepwd

-- Books Table Inserts
INSERT INTO books (title, author, publisher, qtd_reviews, review_score, public, genre_id) VALUES ('O Apanhador no Campo de Centeio','Eu Estou','Muito Bravo', 500, 4.2, false, 1);
INSERT INTO books (title, author, publisher, qtd_reviews, review_score, public, genre_id) VALUES ('O Código Da Vinci','Com quem','Não leu', 800, 4.5, false, 2);
INSERT INTO books (title, author, publisher, qtd_reviews, review_score, public, genre_id) VALUES ('Duna','a doc','e saiu adicionando campo', 600, 4.7, false, 3);

-- Users_Genres Table Inserts
INSERT INTO users_genres (user_id, genre_id) VALUES (1, 1);
INSERT INTO users_genres (user_id, genre_id) VALUES (2, 2);
INSERT INTO users_genres (user_id, genre_id) VALUES (3, 3);

-- Books_Genres Table Inserts
INSERT INTO books_genres (book_id, genre_id) VALUES (1, 1);
INSERT INTO books_genres (book_id, genre_id) VALUES (2, 2);
INSERT INTO books_genres (book_id, genre_id) VALUES (3, 3);

-- Mostra o conteúdo da tabela 'genres'
SELECT * FROM genres;

-- Mostra o conteúdo da tabela 'users'
SELECT * FROM users;

-- Mostra o conteúdo da tabela 'books'
SELECT * FROM books;

-- Mostra o conteúdo da tabela 'users_genres'
SELECT * FROM users_genres;

-- Mostra o conteúdo da tabela 'books_genres'
SELECT * FROM books_genres;

-- Lista usuários por seu gênero favorito
SELECT u.name AS nome_usuario, g.name AS genero_favorito
FROM users u
JOIN users_genres ug ON u.id = ug.user_id
JOIN genres g ON ug.genre_id = g.id;

-- Lista livros por gênero
SELECT b.title AS titulo_livro, g.name AS genero
FROM books b
JOIN books_genres bg ON b.id = bg.book_id
JOIN genres g ON bg.genre_id = g.id;

DROP TABLE IF EXISTS users_genres;
DROP TABLE IF EXISTS books_genres;
DROP TABLE IF EXISTS book_reviews;
-- Exclui a tabela Users
DROP TABLE IF EXISTS users;

-- Exclui a tabela Books
DROP TABLE IF EXISTS books;

-- Exclui a tabela Genres
DROP TABLE IF EXISTS genres;
