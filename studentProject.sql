drop database if exists StudentDB;
create database StudentDB;
use StudentDB;
drop table if exists student;
create table student(
	no char(6) not null primary key ,
    name varchar(10) not null,
    kor int not null,
    eng int not null,
    math int not null,
    total int null,
    avr decimal(7,2) null,
    grade varchar(2) null,
    rate INT null default 0
);
use studentDB;
drop table if exists deletestudent;
create table deleteStudent(
	no char(6) not null,
    name varchar(10) not null,
    kor int not null,
    eng int not null,
    math int not null,
    total int null,
    avr decimal(7,2) null,
    grade varchar(2) null,
    rate INT null, 
    deleteDate datetime
);

    insert into student(no, name, kor, eng, math, total, avr, grade, rate) values('010101', 'keroro', 100, 100, 100, 300, 100, 'A', 0);
   
   insert into student values('010102', 'tom', 100, 100, 100, 300, 100, 'A', 0);
    insert into student values('010103', 'cat', 100, 100, 100, 300, 100, 'A', 0);
    insert into student values('010104', 'dudu', 100, 100, 100, 300, 100, 'A', 0);
    insert into student values('010105', 'nana', 100, 100, 100, 300, 100, 'A', 0);
    
--	DELETE from student where no = '010102';

    drop procedure if exists procedure_insert_student;
    delimiter //
    create procedure procedure_insert_student(
		in in_no char(6),
        in in_name varchar(10),
        in in_kor int,
        in in_eng int,
        in in_math int
    )
    begin
		declare in_total int default 0;
		declare in_avr double default 0.0;
		declare in_grade varchar(2) default null;
		
        set in_total = in_kor + in_eng + in_math;
		set in_avr = in_total/ 3.0;
        set in_grade = 
			case
				when in_avr >= 90.0	then 'A'
                when in_avr >= 80.0	then 'B'
                when in_avr >= 70.0	then 'C'
                when in_avr >= 60.0	then 'D'
                else 'F'
			end;
		insert into student(no, name, kor, eng, math) 
			values (in_no, in_name, in_kor, in_eng, in_math);
		update student set total = in_total, avr = in_avr, grade = in_grade
			where no = in_no;
    end //
    delimiter ;
    
 drop trigger if exists trigger_deleteStudent;
   delimiter !!
 create trigger trigger_deleteStudent
		 after delete
         on student
         for each row
 	begin
 		INSERT INTO deleteStudent VALUES(OLD.no, OLD.name, OLD.kor, OLD.eng, OLD.math, OLD.total, OLD.avr, OLD.grade, OLD.rate, now());
     end !!
    delimiter ;
        
    select * from student;
    select * from deleteStudent;
    