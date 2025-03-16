-- PERIODO de servicio comparado con el dia de su aniversario en el MES/AÑO de TateTo
SELECT
emp.incomedate,
date_part('year', age( DATE(
            EXTRACT(YEAR FROM $P{DateTo}::date) || '-' ||  -- Año de $P{DateTo}
            EXTRACT(MONTH FROM $P{DateTo}::date) || '-' || -- Mes de $P{DateTo}
            EXTRACT(DAY FROM emp.incomedate)               -- Día de emp.incomedate
        ), emp.incomedate )) AS a_servicio,
date_part('month', age(
        DATE(
            EXTRACT(YEAR FROM $P{DateTo}::date) || '-' ||
            EXTRACT(MONTH FROM $P{DateTo}::date) || '-' ||
            EXTRACT(DAY FROM emp.incomedate)
        ), emp.incomedate )) AS m_servicio,
date_part('day', age( DATE(
            EXTRACT(YEAR FROM $P{DateTo}::date) || '-' ||
            EXTRACT(MONTH FROM $P{DateTo}::date) || '-' ||
            EXTRACT(DAY FROM emp.incomedate)
        ), emp.incomedate  )) AS d_servicio
FROM amn_employee emp;
-- PERIODO de servicio comparado con el dia de su cumpleaños en el MES/AÑO de TateTo
SELECT
emp.incomedate,
date_part('year', age( DATE(
            EXTRACT(YEAR FROM $P{DateTo}::date) || '-' ||  -- Año de $P{DateTo}
            EXTRACT(MONTH FROM $P{DateTo}::date) || '-' || -- Mes de $P{DateTo}
            EXTRACT(DAY FROM emp.birthday)               -- Día de emp.incomedate
        ), emp.incomedate )) AS a_servicio,
date_part('month', age(
        DATE(
            EXTRACT(YEAR FROM $P{DateTo}::date) || '-' ||
            EXTRACT(MONTH FROM $P{DateTo}::date) || '-' ||
            EXTRACT(DAY FROM emp.birthday)
        ), emp.incomedate )) AS m_servicio,
date_part('day', age( DATE(
            EXTRACT(YEAR FROM $P{DateTo}::date) || '-' ||
            EXTRACT(MONTH FROM $P{DateTo}::date) || '-' ||
            EXTRACT(DAY FROM emp.birthday)
        ), emp.incomedate  )) AS d_servicio
FROM amn_employee emp;

-- PERIODO de servicio comparado con el ultimo dia del mes solicitado
date_part('year' , age( (DATE_TRUNC('month', $P{DateTo}::date) + INTERVAL '1 month - 1 day'), emp.incomedate )) AS a_servicio_ult,
date_part('month', age( (DATE_TRUNC('month', $P{DateTo}::date) + INTERVAL '1 month - 1 day'), emp.incomedate )) AS m_servicio_ult,
date_part('day'  , age( (DATE_TRUNC('month', $P{DateTo}::date) + INTERVAL '1 month - 1 day'), emp.incomedate )) AS d_servicio_ult
FROM amn_employee emp;