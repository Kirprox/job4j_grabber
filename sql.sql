CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);
CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

INSERT INTO company (id, name) VALUES
(1, 'Apple'),
(2, 'Google'),
(3, 'Microsoft'),
(4, 'Amazon'),
(5, 'Tesla'),
(6, 'Facebook'),
(7, 'IBM'),
(8, 'Oracle'),
(9, 'Intel'),
(10, 'Samsung');

INSERT INTO person (id, name, company_id) VALUES
(1, 'Alice', 1),
(2, 'Bob', 2),
(3, 'Charlie', 3),
(4, 'David', 4),
(5, 'Eve', 5),
(6, 'Frank', 6),
(7, 'Grace', 7),
(8, 'Hank', 8),
(9, 'Ivy', 9),
(10, 'Jack', 10),
(11, 'kir', 1);

select person.name, company.name company from person
join company on(person.company_id = company.id)
where company_id != 5;

select company.name company, count(person) count from company
join person on (person.company_id = company.id)
group by company.name
having count(person) = (
	select max(count) from (
		select count(person) from company
		join person on (person.company_id = company.id)
		group by company.name
	)
);







