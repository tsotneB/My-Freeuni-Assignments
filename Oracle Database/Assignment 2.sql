create sequence id_generator
increment by 1
start with 1
maxvalue 1000
nocache
nocycle;

/


create table Students 
       (student_id number,
        first_name varchar2(25) not null,
        last_name varchar2(25) not null,
        PIN number not null,
        email varchar2(25) not null,
        phone_number number not null,
        constraint stud_stud_id_pk primary key(student_id),
        constraint stud_unique_email unique(email),
        constraint stud_unique_pin unique(pin)
        );
        
/


create table Subjects 
       (subject_id number,
        subject_name varchar2(25) not null,
        subject_exam_date date,
        constraint subject_pk primary key (subject_id),
        constraint subject_unique unique(subject_name),
        constraint subj_ex_unique unique(subject_exam_date));

/


create table student_subject 
       (stud_subj_id number,
        student_id number,
        subject_id number,
        score number,

        constraint stud_subj_pk primary key(stud_subj_id),
        constraint stud_subj_stud_fk foreign key (student_id) references Students(student_id),
        constraint stud_subj_subj_fk foreign key (subject_id) references Subjects(Subject_Id)
        );               
 
/



create table del_student_subject 
       (stud_subj_id number,
        student_id number,
        subject_id number,
        constraint del_stud_subj_pk primary key(stud_subj_id),
        constraint del_stud_subj_stud_fk foreign key (student_id) references Students(student_id),
        constraint del_stud_subj_subj_fk foreign key (subject_id) references Subjects(Subject_Id)
        );               
 
/



create table Universities
       (university_id number,
        university_name varchar2(25) not null,
        constraint uni_pk primary key(university_id),
        constraint uni_unq unique (university_name));

/
        

create table Faculties 
       (faculty_id number,
        faculty_name varchar2(25) not null,
        university_id number,
        constraint fac_pk primary key (faculty_id),
        constraint fac_uni_fk foreign key (university_id) references Universities(university_id));

/


create table stud_faculty 
       (stud_faculty_id number,
        student_id number,
        faculty_id number,
        priority number not null,
        constraint stud_faculty_pk primary key (stud_faculty_id),
        constraint stud_fac_stud_fk foreign key(student_id) references Students (student_id),
        constraint stud_fac_fac_fk foreign key (faculty_id) references Faculties(faculty_Id));

/



create table del_stud_faculty 
       (stud_faculty_id number,
        student_id number,
        faculty_id number,
        constraint del_stud_faculty_pk primary key (stud_faculty_id),
        constraint del_stud_fac_stud_fk foreign key(student_id) references Students (student_id),
        constraint del_stud_fac_fac_fk foreign key (faculty_id) references Faculties(faculty_Id));

/
 

create table Exam_centers
       (exam_center_id number,
        exam_center_address varchar(50) not null ,
        constraint exam_centers_pk primary key (exam_center_id));

/

create table Exam_rooms
       (exam_room_id number,
        exam_center_id number,
        room_no number not null,
        constraint exam_room_pk primary key (exam_room_id),
        constraint exam_room_center_fk foreign key (exam_center_id) references Exam_centers(exam_center_id));

/        

create table Exam_cards
       ( exam_card_id number,
         student_id number,
         exam_center_id number,
         constraint exam_card_pk primary key (exam_card_id),
         constraint exam_card_stud_fk foreign key (student_id) references Students(student_id),
         constraint exam_card_center_id foreign key (exam_center_id) references Exam_centers(exam_center_id));

/


create table Exam_cards_subjects 
       ( card_subj_id number,
         stud_subj_id number,
         exam_room_id number not null,
         exam_card_id number,
         constraint card_subj_pk primary key (card_subj_id),
         constraint card_stud_subj_fk foreign key (stud_subj_id) references student_subject(stud_subj_id),
         constraint card_subj_card_fk foreign key (exam_card_id) references Exam_cards(exam_card_id),
         constraint card_subj_room_fk foreign key (exam_room_id) references Exam_rooms(exam_room_id)
      );

/


 

create table SYS_PARAM 
       (param_id number,
        param_name varchar2(25) not null,
        param_val number not null,
        constraint sys_param_pk primary key (param_id));

/


insert into sys_param values(1,'Update Date',10);
insert into sys_param values(2,'Max Faculties',20);
insert into sys_param values(3,'Max Students in Room',30);

/


/* 'destroying all the objects for testing'
   
   drop sequence id_generator;
   drop table sys_param;
   drop table Exam_cards_subjects;
   drop table Exam_cards;
   drop table Exam_rooms;
   drop table Exam_centers;
   drop table del_stud_faculty;
   drop table stud_faculty;
   drop table Faculties;
   drop table Universities;
   drop table del_student_subject;
   drop table student_subject;
   drop table Subjects;
   drop table Students;
   
*/



create or replace package func_proc_pack is
   exams_start_date date := to_date('15-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'); 
   function check_sign_up (stud Students%rowtype) return boolean;
   function check_faculties (stud_pin Students.pin%type, fac_name Faculties.faculty_name%type,uni_name Universities.university_name%type,priorr number) return boolean;
   function check_subjects (stud_pin Students.pin%type, subj_name Subjects.subject_name%type) return boolean;
   function check_delete_subj (stud_pin Students.pin%type, subj_name Subjects.subject_name%type) return boolean;
   function check_delete_fac (stud_pin Students.pin%type, fac_name Faculties.faculty_name%type,uni_name Universities.University_Name%type) return boolean;
   function exam_center (stud_pin Students.pin%type, center_add Exam_centers.exam_center_address%type) return boolean;
   procedure sign_up(Stud Students%rowtype);
   procedure add_subj(stud_pin Students.pin%type, subj_name Subjects.subject_name%type);
   procedure add_faculty(stud_pin Students.pin%type, fac_name Faculties.faculty_name%type, uni_name Universities.University_Name%type, priorr number);
   procedure delete_subj(stud_pin Students.pin%type, subj_name Subjects.subject_name%type);
   procedure delete_fac(stud_pin Students.pin%type, fac_name Faculties.faculty_name%type,uni_name Universities.University_Name%type);
   procedure add_exam_center(stud_pin Students.pin%type, center_add Exam_centers.exam_center_address%type);
   
end func_proc_pack;
   
/
   
create or replace package body func_proc_pack is
   function check_sign_up (stud Students%rowtype) return boolean is
      pin_found number ;
      email_found number;
      deadline number;
      pin_already_reg exception;
      email_already_reg exception;
      time_out exception;
      begin
        select count(*) into pin_found
        from Students s
        where s.pin = stud.pin;
        select count(*) into email_found
        from Students s
        where s.email = stud.email;
        select param_val into deadline
        from sys_param sp
        where sp.param_name = 'Update Date';
        if (exams_start_date - sysdate < deadline) then
         raise time_out;
         return false;
        end if;
        if pin_found > 0 then
          raise pin_already_reg;
          return false;
          elsif email_found > 0 then
            raise email_already_reg;
            return false;
          else 
            return true;
        end if;
        exception
          when time_out then
            dbms_output.put_line('You can no longer sign up for exams.');
            return false;
          when pin_already_reg then
            dbms_output.put_line('Someone is already registered with your PIN.');
            return false;
          when email_already_reg then
            dbms_output.put_line('Someone is already registered with your email.');
            return false;
   end check_sign_up;          
  
   function check_faculties (stud_pin Students.pin%type, fac_name Faculties.faculty_name%type, uni_name Universities.University_Name%type, priorr number) return boolean is
     stud_id number;
     fac_id number;
     faculties_chosen number;
     fac_found number;
     max_fac number;
     this_fac number;
     this_pr number;
     deadline number;
     stud_found number;
     stud_not_found exception;
     too_many_faculties exception;
     not_found_fac exception;
     already_chosen exception;
     dupl_priorities exception;
     time_out exception;
     begin
       select count(*) into stud_found
       from Students s
       where s.pin = stud_pin;
       if stud_found = 0 then
         raise stud_not_found;
         return false;
         else 
            select s.student_id into stud_id
            from Students s
            where s.pin = stud_pin;
       end if;
       select count(*) into fac_found
       from Faculties f
       join Universities u
       on f.university_id = u.university_id
       where (f.faculty_name = fac_name and u.university_name = uni_name);
       if fac_found = 0 then
         raise not_found_fac;
         return false;
         else
           select f.faculty_id into fac_id
           from Faculties f
           join Universities u
           on f.university_id = u.university_id
           where (u.university_name = uni_name and f.faculty_name = fac_name );
       end if;
      
       select count(*) into faculties_chosen
       from stud_faculty s
       where s.student_id = stud_id;
       select count(*) into this_pr
       from stud_faculty s
       where (s.student_id = stud_id and s.priority = priorr);
       select param_val into max_fac
       from sys_param sp
       where sp.param_name = 'Max Faculties';
       select param_val into deadline
       from sys_param sp
       where sp.param_name = 'Update Date';
       if (exams_start_date - sysdate < deadline) then
         raise time_out;
         return false;
       end if;
       select count(*) into this_fac
       from stud_faculty s
       where (s.student_id = stud_id and s.faculty_id = fac_id);
       if faculties_chosen >= max_fac then
         raise too_many_faculties;
         return false;
          elsif 
              this_fac>0 then 
                         raise already_chosen;
                         return false;
              elsif
                this_pr>0 then
                  raise dupl_priorities;
                  return false;
                  else
                      return true;
          end if;
       exception
         when stud_not_found then
           dbms_output.put_line('Student with that PIN not found.');
           return false;
         when too_many_faculties then
           dbms_output.put_line('You have reached the limit (20) and therefore can not choose any more faculties.');
           return false;
         when not_found_fac then
           dbms_output.put_line('Faculty not found.');
           return false;
         when already_chosen then
           dbms_output.put_line('You have already chosen this faculty.');
           return false;
         when dupl_priorities then
           dbms_output.put_line('You have already chosen faculty with this priotity.');
           return false;
         when time_out then
           dbms_output.put_line('You can no longer change your choices.');
           return false;
     end check_faculties;
      
  function check_subjects (stud_pin Students.pin%type, subj_name Subjects.subject_name%type) return boolean is
    stud_id number;
    subj_id number;
    subj_found number;
    this_subj number;
    deadline number;
    stud_found number;
    stud_not_found exception;
    subj_not_found exception;
    already_reg exception;
    time_out exception;
    begin
      select count(*) into stud_found
      from Students s
      where s.pin = stud_pin;
      if stud_found = 0 then
        raise stud_not_found;
        return false;
        else 
          select s.student_id into stud_id
          from Students s
          where s.pin = stud_pin;
      end if;
      select count(*) into subj_found
      from Subjects s
      where s.subject_name = subj_name;
      if subj_found = 0 then
        raise subj_not_found;
        return false;
        else 
          select s.subject_id into subj_id
          from Subjects s
          where s.subject_name = subj_name;
      end if;
      select count(*) into this_subj
      from student_subject ss
      where (ss.student_id = stud_id and ss.subject_id = subj_id);
      select param_val into deadline
      from sys_param sp
      where sp.param_name = 'Update Date';
      if (exams_start_date - sysdate < deadline) then
         raise time_out;
         return false;
      end if;
      select count(*) into stud_found
      from Students s
      where s.student_id = stud_id;
     if this_subj > 0 then
        raise already_reg;
        return false;
      else
        return true;
      end if;
      exception  
         when stud_not_found then
           dbms_output.put_line('Student with that PIN not found.');
           return false;
         when subj_not_found then
           dbms_output.put_line('Subject with that name not found.');
           return false;
         when already_reg then
           dbms_output.put_line('You have already chosen this subject.');
           return false;     
         when time_out then
           dbms_output.put_line('You can no longer change your choices.');
           return false;
     end check_subjects;
         
   function check_delete_subj (stud_pin Students.pin%type, subj_name Subjects.subject_name%type) return boolean is
   stud_id number;
   subj_id number;
   subj_found number;
   subj_chosen number;  
   stud_found number;
   deadline number;
   stud_not_found exception;
   subj_not_found exception;
   subj_not_ch exception;
   time_out exception;
    begin 
      select count(*) into stud_found
      from Students s
      where s.pin = stud_pin;
      if stud_found = 0 then
        raise stud_not_found;
        return false;
        else 
          select s.student_id into stud_id
          from Students s
          where s.pin = stud_pin;
      end if;
      select count(*) into subj_found
      from Subjects s
      where s.subject_name = subj_name;
      if subj_found = 0 then
        raise subj_not_found;
        return false;
        else 
          select s.subject_id into subj_id
          from Subjects s
          where s.subject_name = subj_name;
      end if;
      select count(*) into subj_chosen
      from student_subject ss
      where (ss.student_id = stud_id and ss.subject_id = subj_id);
      select param_val into deadline
      from sys_param sp
      where sp.param_name = 'Update Date';
      if (exams_start_date - sysdate < deadline) then
         raise time_out;
         return false;
      end if;
      if subj_chosen = 0 then
            raise subj_not_ch;
            return false;
      end if;
      return true;
      exception 
        when stud_not_found then
           dbms_output.put_line('Student with that PIN not found.');
           return false;
         when subj_not_found then
           dbms_output.put_line('Subject with that name not found.');
           return false;
         when subj_not_ch then
           dbms_output.put_line('You have not chosen this subject.');
           return false;     
         when time_out then
           dbms_output.put_line('You can no longer change your choices.');
           return false;
   end check_delete_subj;
       
   function check_delete_fac (stud_pin Students.pin%type, fac_name Faculties.faculty_name%type,uni_name Universities.University_Name%type) return boolean is
    stud_id number;
    fac_id number;
    fac_exists number;
    fac_chosen number;  
    student_exists number;
    deadline number;
    stud_not_ex exception;
    fac_not_ex exception;
    fac_not_ch exception;
    time_out exception;
    begin 
      select count(*) into student_exists
       from Students s
       where s.pin = stud_pin;
       if student_exists = 0 then
         raise stud_not_ex;
         return false;
         else 
            select s.student_id into stud_id
            from Students s
            where s.pin = stud_pin;
       end if;
       select count(*) into fac_exists
       from Faculties f
       join Universities u
       on f.university_id = u.university_id
       where (f.faculty_name = fac_name and u.university_name = uni_name);
       if fac_exists = 0 then
         raise fac_not_ex;
         return false;
         else
           select f.faculty_id into fac_id
           from Faculties f
           join Universities u
           on f.university_id = u.university_id
           where (f.faculty_name = fac_name and u.university_name = uni_name);
       end if;
      select count(*) into fac_chosen
      from stud_faculty sf
      where (sf.student_id = stud_id and sf.faculty_id = fac_id);
      select param_val into deadline
      from sys_param sp
      where sp.param_name = 'Update Date';
      if (exams_start_date - sysdate < deadline) then
         raise time_out;
         return false;
      end if;
      if fac_chosen = 0 then
         raise fac_not_ch;
         return false;
      end if;
      return true;
      exception 
        when stud_not_ex then
           dbms_output.put_line('Student with that PIN not found.');
           return false;
         when fac_not_ex then
           dbms_output.put_line('Faculty not found.');
           return false;
         when fac_not_ch then
           dbms_output.put_line('You have not chosen this faculty.');
           return false;     
         when time_out then
           dbms_output.put_line('You can no longer change your choices.');
           return false;
   end check_delete_fac;
   
   function exam_center (stud_pin Students.pin%type, center_add Exam_centers.exam_center_address%type) return boolean is
     stud_id number;
     center_id number;
     center_exists number;
     center_chosen number;  
     student_exists number;
     deadline number;
     stud_not_ex exception;
     center_not_ex exception;
     center_ch exception;
     time_out exception;
    begin
      select count(*) into center_exists
      from Exam_centers ec
      where ec.exam_center_address = center_add;
      if (center_exists = 0) then
        raise center_not_ex;
        return false;
        else
          select ec.exam_center_id into center_id
          from Exam_centers ec
          where ec.exam_center_address=center_add;
      end if;
      select count(*) into student_exists
      from Students s
      where s.pin = stud_pin;
      if (student_exists = 0) then
        raise stud_not_ex;
        return false;
        else
          select s.student_id into stud_id
          from Students s
          where s.pin = stud_pin;
      end if;
      select count(*) into center_chosen 
      from Exam_cards ec
      where ec.student_id = stud_id;
      select param_val into deadline
      from sys_param sp
      where sp.param_name = 'Update Date';
      if (exams_start_date - sysdate < deadline) then
         raise time_out;
         return false;
      end if;
      if center_chosen > 0 then
            raise center_ch;
            return false;
            else 
              return true;
      end if;
      exception 
        when stud_not_ex then
           dbms_output.put_line('Student with that PIN not found.');
           return false;
         when center_not_ex then
           dbms_output.put_line('Exam center with that address not found.');
           return false;
         when center_ch then
           dbms_output.put_line('You have already chosen exam center.');
           return false;     
         when time_out then
           dbms_output.put_line('You can no longer change your choices.');
           return false;
   end exam_center;
   
   procedure sign_up (stud Students%rowtype) is
     begin
       if (check_sign_up(stud)) then
          insert into Students values stud;
          end if;
   end sign_up;
   
   procedure add_subj(stud_pin Students.pin%type, subj_name Subjects.subject_name%type) is
     stud_id number;
     subj_id number;
     room_no number;
     room_id number;
     counter number;
     exm_cent number;
     stud_subj_id number; 
     exam_card_id number;
     card_id number;
     counter2 number;
     room_limit number;
     begin
       if (check_subjects(stud_pin, subj_name)) then
         select s.student_id into stud_id
         from Students s
         where s.pin = stud_pin;
         select s.subject_id into subj_id
         from Subjects s
         where s.subject_name = subj_name;
         stud_subj_id := id_generator.nextval;
         insert into student_subject values (stud_subj_id,stud_id,subj_id,trunc(dbms_random.value(1,100)));
         select ec.exam_center_id into exm_cent
         from Exam_cards ec
         where ec.student_id = stud_id;
         select ec1.exam_card_id into card_id
         from Exam_cards ec1
         where ec1.student_id = stud_id;
         select count(*) into counter
         from Exam_cards ecs
         join student_subject s_s 
         on ecs.student_id = s_s.student_id
         where (subj_id = s_s.subject_id and ecs.exam_center_id = exm_cent); 
         select param_val into room_limit
         from sys_param
         where param_name = 'Max Students in Room';
         room_no := floor(counter/room_limit) + 1;
         
         exam_card_id := id_generator.nextval;
        
         select count(*) into counter2
         from Exam_rooms er
         where (er.exam_center_id = exm_cent and er.room_no = room_no);
         if counter2 = 0 then
            room_id := id_generator.nextval;
            insert into Exam_rooms values(
                room_id,exm_cent,room_no);
            else 
              select er.exam_room_id into room_id
              from Exam_rooms er
              where (er.exam_center_id = exm_cent and er.room_no = room_no);
         end if;
         insert into Exam_cards_subjects values( 
           id_generator.nextval,stud_subj_id, room_id,card_id);
         end if;
   end add_subj;
   
   procedure add_exam_center(stud_pin Students.pin%type, center_add Exam_centers.exam_center_address%type) is
     center_id number;
     stud_id number;
     begin
       if (exam_center(stud_pin,center_add)) then
         select ec.exam_center_id into center_id
         from Exam_centers ec
         where ec.exam_center_address=center_add;
         select s.student_id into stud_id
         from Students s
         where s.pin = stud_pin;
         insert into Exam_cards values (id_generator.nextval,stud_id,center_id);
         end if;
   end add_exam_center;      
         
   procedure add_faculty(stud_pin Students.pin%type, fac_name Faculties.faculty_name%type, uni_name Universities.University_Name%type, priorr number) is
     stud_id number;
     fac_id number;
     begin
       if (check_faculties(stud_pin,fac_name,uni_name,priorr)) then
         select s.student_id into stud_id
         from Students s
         where s.pin = stud_pin; 
         select f.faculty_id into fac_id
         from Faculties f
         join Universities u
         on f.university_id = u.university_id
         where (f.faculty_name = fac_name and u.university_name = uni_name);
         insert into stud_faculty values(id_generator.nextval,stud_id,fac_id,priorr);
         end if;
   end add_faculty;
   
   procedure delete_subj(stud_pin Students.pin%type, subj_name Subjects.subject_name%type) is
     deleted_id number;
     stud_id number;
     subj_id number;
     begin
       if (check_delete_subj(stud_pin,subj_name)) then
         select s.student_id into stud_id
         from Students s
         where s.pin = stud_pin;
         select s.subject_id into subj_id
         from Subjects s
         where s.subject_name = subj_name;
         select stud_subj_id into deleted_id
         from student_subject
         where (student_id = stud_id and subject_id = subj_id);
         insert into del_student_subject 
         values (deleted_id,stud_id,subj_id);
         delete exam_cards_subjects ecs
         where ecs.stud_subj_id = deleted_id;
         delete student_subject
         where (student_id = stud_id and subject_id = subj_id);
      end if;
   end delete_subj;
         
   procedure delete_fac(stud_pin Students.pin%type, fac_name Faculties.faculty_name%type,uni_name Universities.University_Name%type) is
     deleted_id number;
     stud_id number;
     fac_id number;
     begin
       if (check_delete_fac(stud_pin,fac_name,uni_name)) then
         select s.student_id into stud_id
         from Students s
         where s.pin = stud_pin; 
         select f.faculty_id into fac_id
         from Faculties f
         join Universities u
         on f.university_id = u.university_id
         where (f.faculty_name = fac_name and u.university_name = uni_name);
         select stud_faculty_id into deleted_id 
         from stud_faculty
         where (student_id = stud_id and faculty_id = fac_id);
         delete stud_faculty
         where (student_id = stud_id and faculty_id = fac_id);
         insert into del_stud_faculty
         values (deleted_id,stud_id,fac_id);
       end if;
  end delete_fac;
  
end func_proc_pack;
  
/

create or replace view uni_rank as
  select (select uni.university_name as 
       from Universities uni where uni.university_id = u.university_id) as "Name", 
       count(u.university_id) AS "Student Amount"
       from stud_faculty sf
       join Faculties f
       on sf.faculty_id = f.faculty_id
       join Universities u
       on f.university_id = u.university_id
       group by u.university_id
       order by 2 desc;
/

create or replace view student_rank as
  select stsb.student_id,s.subject_name,stsb.score
  from student_subject stsb
  join subjects s
  on stsb.subject_id = s.subject_id
  order by (select avg(stsb1.score)
            from student_subject stsb1
            where stsb.student_id = stsb1.student_id
            group by stsb1.student_id) Desc, s.subject_name asc;
/

insert into Subjects values (id_generator.nextval,'Georgian',to_date('15-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'));
insert into Subjects values (id_generator.nextval,'English',to_date('16-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'));
insert into Subjects values (id_generator.nextval,'Math',to_date('17-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'));
insert into Subjects values (id_generator.nextval,'Biology',to_date('18-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'));
insert into Subjects values (id_generator.nextval,'Physics',to_date('19-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'));
insert into Subjects values (id_generator.nextval,'History',to_date('20-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'));
insert into Subjects values (id_generator.nextval,'Geography',to_date('21-02-2021 00:00:00','dd-mm-yyyy hh24:mi:ss'));


insert into Universities values (id_generator.nextval,'Free University');
insert into Universities values (id_generator.nextval,'TSU');
insert into Universities values (id_generator.nextval,'Ilia State University');
insert into Universities values (id_generator.nextval,'Caucasus University');

insert into Faculties values (id_generator.nextval,'MACS',8);
insert into Faculties values (id_generator.nextval,'ESM',8);
insert into Faculties values (id_generator.nextval,'VADS',8);
insert into Faculties values (id_generator.nextval,'CS',9);
insert into Faculties values (id_generator.nextval,'Business',9);
insert into Faculties values (id_generator.nextval,'CS',10);
insert into Faculties values (id_generator.nextval,'Business',10);
insert into Faculties values (id_generator.nextval,'CS',11);
insert into Faculties values (id_generator.nextval,'Business',11);

insert into Exam_centers values (id_generator.nextval,'Tbilisi');
insert into Exam_centers values (id_generator.nextval,'Kutaisi');
insert into Exam_centers values (id_generator.nextval,'Batumi');

declare
   stud Students%rowtype; 
   stud1 Students%rowtype;
   stud2 Students%rowtype;
   stud3 Students%rowtype;
   stud4 Students%rowtype;
   stud5 Students%rowtype;
begin 
  stud.student_id := id_generator.nextval;
  stud.first_name := 'Tsotne';
  stud.last_name := 'Babunashvili';
  stud.pin := 60001153411;
  stud.email := 'tbabu19@freeuni.edu.ge';
  stud.phone_number := 555685305;
  func_proc_pack.sign_up(stud);
  stud1.student_id := id_generator.nextval;
  stud1.first_name := 'Tsotne';
  stud1.last_name := 'Babunashvili';
  stud1.pin := 60001153011;
  stud1.email := 'tbabu19@freeuni.edu.ge';
  stud1.phone_number := 555685305;
  func_proc_pack.sign_up(stud1);
  stud2.student_id := id_generator.nextval;
  stud2.first_name := 'Tsotne';
  stud2.last_name := 'Babunashvili';
  stud2.pin := 60001153411;
  stud2.email := 'tbabu19@freuni.edu.ge';
  stud2.phone_number := 555685305;
  func_proc_pack.sign_up(stud2);
  stud3.student_id := id_generator.nextval;
  stud3.first_name := 'Tsotne';
  stud3.last_name := 'Babunashvili';
  stud3.pin := 60001152531;
  stud3.email := 'tssbabu19@freeuni.edu.ge';
  stud3.phone_number := 555682705;
  func_proc_pack.sign_up(stud3);
  stud4.student_id := id_generator.nextval;
  stud4.first_name := 'Tsotne';
  stud4.last_name := 'Babunashvili';
  stud4.pin := 60001152531;
  stud4.email := 'tbassbu19@freuni.edu.ge';
  stud4.phone_number := 555685305;
  func_proc_pack.sign_up(stud4);
  stud5.student_id := id_generator.nextval;
  stud5.first_name := 'Tsotne';
  stud5.last_name := 'Babunashvili';
  stud5.pin := 60001122531;
  stud5.email := 'tssbabu19@freeuni.edu.ge';
  stud5.phone_number := 555682705;
  func_proc_pack.sign_up(stud5); 
  func_proc_pack.add_exam_center(stud.pin,'Zugdidi'); -- exam center not found
  func_proc_pack.add_exam_center(stud.pin,'Kutaisi');
  func_proc_pack.add_exam_center(stud.pin,'Tbilisi'); -- exam center already chosen
  func_proc_pack.add_exam_center(stud3.pin,'Tbilisi');
  func_proc_pack.add_exam_center(stud3.pin,'Kutaisi'); -- exam_center already chosen
  func_proc_pack.add_subj(stud.pin,'Math');
  func_proc_pack.add_subj(stud3.pin,'Math'); -- student doesn't exist
  func_proc_pack.delete_subj(stud.pin,'Math'); 
  func_proc_pack.add_faculty(stud.pin,'MACS','Free University',1);
  func_proc_pack.delete_fac(stud.pin,'MACS','Free University');
  --for exceptions;
  func_proc_pack.add_faculty(stud.pin,'MACS','Free University',1);
  func_proc_pack.add_faculty(stud.pin,'MACS','Free University',2); --  faculty already chosen
  func_proc_pack.add_faculty(stud.pin,'ESM','Free University',1); --  priority already chosen
  func_proc_pack.add_faculty(stud.pin,'RAND','RAND UNI',1); -- faculty doesn't exist
  func_proc_pack.add_faculty(3231242,'ESM','Free University',1); -- student doesn't exist
  func_proc_pack.delete_fac(stud.pin,'ESM','Free University'); -- students hasn't chosen this faculty
  func_proc_pack.delete_fac(stud.pin,'RAND','RAND UNI'); -- faculty doesn't exist
  func_proc_pack.delete_fac(123456,'ESM','Free University'); -- student doesn't exist
  func_proc_pack.add_subj(stud.pin,'MSD'); -- subject doesn't exist
  func_proc_pack.add_subj(123432,'Math'); -- student doesn't exist
  func_proc_pack.add_subj(stud3.pin,'Math'); -- subject already chosen
  func_proc_pack.delete_subj(stud.pin,'MSD'); -- subject_doesn't exist
  func_proc_pack.delete_subj(3243,'Math'); -- student doesn't exist
  func_proc_pack.delete_subj(stud.pin,'Math'); -- subject not chosen
  func_proc_pack.add_faculty(stud.pin,'ESM','Free University',2);
  func_proc_pack.add_faculty(stud3.pin,'CS','Caucasus University',1);
  func_proc_pack.add_subj(stud.pin,'Math');
  func_proc_pack.add_subj(stud3.pin,'Math');
  func_proc_pack.add_subj(stud3.pin,'Biology');
  func_proc_pack.delete_subj(stud3.pin,'Biology');
end;

/*

select *
from students_rank

select *
from students;
select *
from student_subject;


select * 
from students_rank;

select * 
from uni_rank; */
/*
select *
from student_subject;

select * 
from faculties;

select *
from stud_faculty;

select *
from del_stud_faculty;

select * 
from del_student_subject;
*/
/*
select *
from Students; 

select * 
from subjects;

*/
/*

select * from students;
select * from student_subject;

select *
from Exam_cards_subjects;

select * 
from Exam_cards;

select * 
from Exam_rooms;  */

/*select *
           from Faculties f
           join Universities u
           on f.uni_id = u.uni_id;
          select *
           from Faculties f
           join Universities u
           on f.uni_id = u.uni_id
           where (f.faculty_name = 'CS' and u.uni_name = 'Caucasus University');
           */
           
