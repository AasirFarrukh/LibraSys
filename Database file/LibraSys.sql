Create database final_project
use final_project

CREATE TABLE book (
	id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    orcid VARCHAR(16) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    genre VARCHAR(50),
    date DATE,
    status Boolean
);

CREATE TABLE member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(50) NOT NULL,
    phoneNo VARCHAR(20) NOT NULL,
    cnic VARCHAR(15) NOT NULL
);

CREATE INDEX isbn_index ON book(isbn);

CREATE TABLE loan (
    loanID INT AUTO_INCREMENT PRIMARY KEY,
    memberID INT,
    bookID VARCHAR(13),
    loanDate DATE,
    returnDate DATE,
    renewalCount INT DEFAULT 0,
    FOREIGN KEY (memberID) REFERENCES member(id),
    FOREIGN KEY (bookID) REFERENCES book(isbn)
);

DELIMITER //
CREATE TRIGGER before_loan_insert
BEFORE INSERT ON loan
FOR EACH ROW
BEGIN
    SET NEW.loanDate = COALESCE(NEW.loanDate, CURDATE());
    SET NEW.returnDate = COALESCE(NEW.returnDate, DATE_ADD(CURDATE(), INTERVAL 7 DAY));
END;
//
DELIMITER ;

INSERT INTO book (title, author, orcid, isbn, genre, date, status) VALUES
('The Da Vinci Code', 'Dan Brown', '0000-0001-2345', 'A1', 'Mystery', '2003-04-01', true),
('The Kite Runner', 'Khaled Hosseini', '0000-0001-2346', 'A2', 'Fiction', '2003-05-29', true),
('A Thousand Splendid Suns', 'Khaled Hosseini', '0000-0001-2346', 'A3', 'Fiction', '2007-05-22', true),
('Harry Potter and the Deathly Hallows', 'J.K. Rowling', '0000-0001-2347', 'A4', 'Fantasy', '2007-07-21', true),
('Twilight', 'Stephenie Meyer', '0000-0001-2348', 'A5', 'Fantasy', '2005-10-05', true),
('New Moon', 'Stephenie Meyer', '0000-0001-2348', 'A6', 'Fantasy', '2006-09-06', true),
('Eclipse', 'Stephenie Meyer', '0000-0001-2348', 'A7', 'Fantasy', '2007-08-07', true),
('Breaking Dawn', 'Stephenie Meyer', '0000-0001-2348', 'A8', 'Fantasy', '2008-08-02', true),
('The Hunger Games', 'Suzanne Collins', '0000-0001-2349', 'A9', 'Dystopian', '2008-09-14', true),
('Catching Fire', 'Suzanne Collins', '0000-0001-2349', 'A10', 'Dystopian', '2009-09-01', true),
('Mockingjay', 'Suzanne Collins', '0000-0001-2349', 'A11', 'Dystopian', '2010-08-24', true),
('The Girl on the Train', 'Paula Hawkins', '0000-0001-2350', 'A12', 'Thriller', '2015-01-13', true),
('Gone Girl', 'Gillian Flynn', '0000-0001-2351', 'A13', 'Thriller', '2012-06-05', true),
('Fifty Shades of Grey', 'E.L. James', '0000-0001-2352', 'A14', 'Romance', '2011-05-25', true),
('Fifty Shades Darker', 'E.L. James', '0000-0001-2352', 'A15', 'Romance', '2012-09-13', true),
('Fifty Shades Freed', 'E.L. James', '0000-0001-2352', 'A16', 'Romance', '2012-01-17', true),
('The Maze Runner', 'James Dashner', '0000-0001-2353', 'A17', 'Dystopian', '2009-10-06', true),
('The Scorch Trials', 'James Dashner', '0000-0001-2353', 'A18', 'Dystopian', '2010-09-18', true),
('The Death Cure', 'James Dashner', '0000-0001-2353', 'A19', 'Dystopian', '2011-10-11', true),
('The Fault in Our Stars', 'John Green', '0000-0001-2354', 'A20', 'Young Adult', '2012-01-10', true),
('Looking for Alaska', 'John Green', '0000-0001-2354', 'A21', 'Young Adult', '2005-03-03', true),
('Paper Towns', 'John Green', '0000-0001-2354', 'A22', 'Young Adult', '2008-10-16', true),
('An Abundance of Katherines', 'John Green', '0000-0001-2354', 'A23', 'Young Adult', '2006-09-21', true),
('Divergent', 'Veronica Roth', '0000-0001-2355', 'A24', 'Dystopian', '2011-04-25', true),
('Insurgent', 'Veronica Roth', '0000-0001-2355', 'A25', 'Dystopian', '2012-05-01', true),
('Allegiant', 'Veronica Roth', '0000-0001-2355', 'A26', 'Dystopian', '2013-10-22', true),
('The Alchemist', 'Paulo Coelho', '0000-0001-2356', 'A27', 'Adventure', '1988-01-01', true),
('Brida', 'Paulo Coelho', '0000-0001-2356', 'A28', 'Adventure', '1990-01-01', true),
('The Devil and Miss Prym', 'Paulo Coelho', '0000-0001-2356', 'A29', 'Adventure', '2000-01-01', true),
('The Valkyries', 'Paulo Coelho', '0000-0001-2356', 'A30', 'Adventure', '1992-01-01', true),
('The Lord of the Rings: The Fellowship of the Ring', 'J.R.R. Tolkien', '0000-0001-2362', 'A31', 'Fantasy', '2000-03-18', true),
('The Hobbit', 'J.R.R. Tolkien', '0000-0001-2362', 'A32', 'Fantasy', '1997-09-21', true),
('The Zahir', 'Paulo Coelho', '0000-0001-2356', 'A33', 'Adventure', '2005-01-01', true),
('The Adventures of Sherlock Holmes', 'Arthur Conan Doyle', '0000-0001-2361', 'A34', 'Mystery', '1992-10-14', true),
('Great Expectations', 'Charles Dickens', '0000-0001-2360', 'A35', 'Historical Fiction', '1991-08-01', true),
('Harry Potter and the Philosopher’s Stone', 'J.K. Rowling', '0000-0001-2347', 'A36', 'Fantasy', '1997-06-26', true),
('Harry Potter and the Chamber of Secrets', 'J.K. Rowling', '0000-0001-2347', 'A37', 'Fantasy', '1998-07-02', true),
('Harry Potter and the Prisoner of Azkaban', 'J.K. Rowling', '0000-0001-2347', 'A38', 'Fantasy', '1999-07-08', true),
('Harry Potter and the Goblet of Fire', 'J.K. Rowling', '0000-0001-2347', 'A39', 'Fantasy', '2000-07-08', true),
('Harry Potter and the Order of the Phoenix', 'J.K. Rowling', '0000-0001-2347', 'A40', 'Fantasy', '2003-06-21', true),
('Harry Potter and the Half-Blood Prince', 'J.K. Rowling', '0000-0001-2347', 'A41', 'Fantasy', '2005-07-16', true),
('The Testaments', 'Margaret Atwood', '0000-0001-2357', 'A42', 'Dystopian', '2019-09-10', true),
('The Handmaid Tale', 'Margaret Atwood', '0000-0001-2357', 'A43', 'Dystopian', '1985-01-01', true),
('Catching Fire', 'Suzanne Collins', '0000-0001-2358', 'A44', 'Science Fiction', '2009-09-01', true),
('The Hunger Games', 'Suzanne Collins', '0000-0001-2358', 'A45', 'Science Fiction', '2008-09-14', true),
('Carrie', 'Stephen King', '0000-0001-2365', 'A46', 'Horror', '1974-04-05', true),
('\'Salem\'s Lot', 'Stephen King', '0000-0001-2365', 'A47', 'Horror', '1975-10-17', true),
('The Shining', 'Stephen King', '0000-0001-2365', 'A48', 'Horror', '1977-01-28', true),
('The Stand', 'Stephen King', '0000-0001-2365', 'A49', 'Horror', '1978-10-03', true),
('The Dead Zone', 'Stephen King', '0000-0001-2365', 'A50', 'Horror', '1979-08-30', true);

INSERT INTO member (name, email, password, address, phoneNo, cnic) VALUES
('John Doe', 'john.doe@example.com', 'password1', '123 Main St', '123-456-7890', '12345-1234567-1'),
('Jane Smith', 'jane.smith@example.com', 'password2', '456 Maple Ave', '098-765-4321', '12345-1234567-2'),
('Bob Johnson', 'bob.johnson@example.com', 'password3', '789 Oak Dr', '456-789-0123', '12345-1234567-3'),
('Alice Williams', 'alice.williams@example.com', 'password4', '321 Elm St', '321-654-0987', '12345-1234567-4'),
('Charlie Brown', 'charlie.brown@example.com', 'password5', '654 Pine Ln', '654-321-9870', '12345-1234567-5'),
('Diana Miller', 'diana.miller@example.com', 'password6', '987 Birch Pl', '987-654-3210', '12345-1234567-6'),
('Frank Wilson', 'frank.wilson@example.com', 'password7', '234 Cedar St', '234-567-8901', '12345-1234567-7'),
('Grace Moore', 'grace.moore@example.com', 'password8', '567 Spruce Ave', '567-890-1234', '12345-1234567-8'),
('Harry Taylor', 'harry.taylor@example.com', 'password9', '890 Redwood Dr', '890-123-4567', '12345-1234567-9'),
('Irene Anderson', 'irene.anderson@example.com', 'password10', '123 Willow St', '123-890-4567', '12345-1234567-0');

create table person (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(50) NOT NULL,
    phoneNo VARCHAR(20) NOT NULL,
    cnic VARCHAR(15) NOT NULL
    );
    
    INSERT INTO person (name, email, password, address, phoneNo, cnic) VALUES
('patron1', 'patron@example.com', '123456789', '123 Main St', '123-456-7890', '12345-1234567-1');

select * from book;
select * from member;
select * from loan;