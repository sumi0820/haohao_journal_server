-- Add priority and time tracking columns to tasks table
ALTER TABLE
 tasks
ADD
 COLUMN description TEXT,
ADD
 COLUMN priority VARCHAR(10) NOT NULL DEFAULT 'MEDIUM',
ADD
 COLUMN estimated_hours FLOAT NOT NULL DEFAULT 0,
ADD
 COLUMN actual_hours FLOAT NOT NULL DEFAULT 0;

-- Update existing tasks to set default values
UPDATE
 tasks
SET
 priority = 'MEDIUM',
 estimated_hours = 0,
 actual_hours = 0
WHERE
 priority IS NULL
 OR estimated_hours IS NULL
 OR actual_hours IS NULL;

-- Add check constraint for priority
ALTER TABLE
 tasks
ADD
 CONSTRAINT tasks_priority_check CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT'));

-- Add check constraint for estimated_hours and actual_hours
ALTER TABLE
 tasks
ADD
 CONSTRAINT tasks_estimated_hours_check CHECK (estimated_hours >= 0),
ADD
 CONSTRAINT tasks_actual_hours_check CHECK (actual_hours >= 0);
