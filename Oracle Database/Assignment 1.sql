select substr(first_name,1,1) || '.' || rpad(substr(last_name,1,1),length(last_name),'*') name, 
       trunc(months_between(sysdate,hire_date)/3) "QUARTERS WORKED",     
       case 
         when job_id in ('IT_PROG','ST_CLERK') then to_char(emp.salary * nvl(emp.commission_pct,0.2),'$999,999,999.00')
         else nvl2(emp.commission_pct,to_char(emp.salary * emp.commission_pct,'$999,999,999.00'),'N/A')
           end bonus                       
from employees emp
where (months_between(sysdate,emp.hire_date)/12 >5 
and months_between(sysdate,emp.hire_date)/12 < 20)
or emp.salary > 3000 
or ( last_name like '%a%' or last_name like '%A%')
or ( emp.department_id not in (20,40,90) or emp.department_id is null) 
order by case 
              when bonus != 'N/A' then bonus 
          end 
          desc nulls last; 
          
select dep.department_name dep_name,
c.country_name || ',' || loc.city || ',' || loc.street_address address,
(select count(*)
 from employees emp
 where emp.department_id = dep.department_id) employees,
(select count (distinct job_id) 
 from employees emp
 where emp.department_id = dep.department_id) unique_jobs,
(select sum(emp.salary)
 from employees emp
 where emp.department_id = dep.department_id) avg_dep_sal,
(select sum(emp.salary)
 from employees emp) total_sal
from employees emp
right join departments dep
on emp.employee_id = dep.manager_id
join locations loc
on dep.location_id = loc.location_id
join countries c
on loc.country_id = c.country_id
join regions reg
on c.region_id = reg.region_id
where reg.region_name in ('Europe','Asia')
and (loc.city not in ('Roma','Venice') 
and c.country_name != 'Germany')
and 1 = (select count(*)
         from departments dep
         join locations loc
         on dep.location_id = loc.location_id
         where loc.country_id = c.country_id
        )
and (emp.last_name not in ('Greenberg', 'Urman') or emp.last_name is null)
and 2 <= (select count (distinct emp.job_id)
     from employees e3
     where e3.department_id = dep.department_id) 
order by dep.department_id desc;

select emp.first_name first_name, 
emp.last_name last_name, 
emp.job_id job_id,
emp.department_id dep_no,
dep.department_name, 
case when emp.salary < 5000 then 'low_sal'
     when emp.salary >= 5000 and emp.salary < 10000 then 'middle_sal'
     else 'high_sal' end salary
from employees emp 
join departments dep
on emp.department_id = dep.department_id
where (select avg(emp.salary)
       from employees emp
       where emp.department_id = dep.department_id) 
       >= 100 + (select avg(emp.salary)
                 from employees emp
                 where emp.department_id = 50)
order by case when dep.department_id = 80 then 1
              when dep.department_id = 20 then 3
              else 2 end, 
              (select avg(emp.salary)
               from employees emp
               where emp.department_id = dep.department_id) asc;
                                                                            
select c.country_id "COUNTRY CODE",
case when (select count(*) 
           from employees em
           join departments de
           on em.department_id = de.department_id 
           join locations lo
           on lo.location_id = de.location_id
           where lo.country_id = c.country_id) = 0 then 'N/A'
           else to_char(emp.department_id) end dep_no,
to_char((select min(em.salary)
         from employees em
         where em.department_id = emp.department_id and (sysdate-em.hire_date)/7>15),'999,999,999.00') min_salary,
to_char((select max(em.salary)
         from employees em
         where em.department_id = emp.department_id
         ),'999,999,999.00') max_salary,
case 
    when (select count(*) 
          from employees e
          join departments d
          on d.department_id = e.department_id
          join locations l
          on d.location_id = l.location_id
          where l.country_id = c.country_id) = 0 then 'N/A'
    when (select max(e1.salary) 
          from employees e1
          where emp.department_id = e1.department_id) > 
          (select avg (max(e2.salary))
           from employees e2
           join departments d2
           on e2.department_id = d2.department_id
           join locations l2
           on l2.location_id = d2.location_id
           where l2.country_id = c.country_id
           group by d2.department_id) then 'HIGH SALARY'
       ELSE 'LOW SALARY' END SALARY_INFO,
(
select count(distinct l4.city)
                       from employees e4
                       join departments d4
                       on e4.department_id = d4.department_id
                       join locations l4
                       on d4.location_id = l4.location_id
                       where l4.country_id = c.country_id
) cities
from employees emp
join departments dep
on emp.department_id = dep.department_id
join locations loc
on dep.location_id = loc.location_id
right join countries c
on loc.country_id = c.country_id
group by c.country_id,emp.department_id
order by case when dep_no != 'N/A' then 0 else 1 end, 
          c.country_id;
   
