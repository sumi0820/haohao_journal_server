CREATE TABLE epics (
 id BIGSERIAL PRIMARY KEY,
 name VARCHAR(255) NOT NULL,
 color VARCHAR(255),
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL
);

CREATE TABLE tasks (
 id BIGSERIAL PRIMARY KEY,
 sprint_id BIGINT NOT NULL,
 epic_id BIGINT NOT NULL,
 title VARCHAR(255) NOT NULL,
 memo TEXT,
 planned_date TIMESTAMP NOT NULL,
 completed_date TIMESTAMP,
 status VARCHAR(10) NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 FOREIGN KEY (sprint_id) REFERENCES sprints(id),
 FOREIGN KEY (epic_id) REFERENCES epics(id)
);

CREATE TABLE daily_reviews (
 id BIGSERIAL PRIMARY KEY,
 sprint_id BIGINT NOT NULL,
 date TIMESTAMP NOT NULL,
 done_summary TEXT NOT NULL,
 feeling_summary TEXT NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 FOREIGN KEY (sprint_id) REFERENCES sprints(id)
);

CREATE TABLE sprint_reviews (
 id BIGSERIAL PRIMARY KEY,
 sprint_id BIGINT NOT NULL,
 doya_summary TEXT NOT NULL,
 thanks_summary TEXT NOT NULL,
 continue_summary TEXT NOT NULL,
 stuck_summary TEXT NOT NULL,
 change_summary TEXT NOT NULL,
 created_at TIMESTAMP NOT NULL,
 updated_at TIMESTAMP NOT NULL,
 FOREIGN KEY (sprint_id) REFERENCES sprints(id)
);
