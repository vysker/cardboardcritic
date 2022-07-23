create table game (
  id serial primary key,
  name text,
  short_description text,
  description text,
  designer text,
  release_date date,
  average int,
  median int,
  recommended int,
  slug text,
  image text
);

create table critic (
  id serial primary key,
  name text
);

create table outlet (
  id serial primary key,
  name text,
  website text
);

create table review (
  id serial primary key,
  game_id int references game(id),
  critic_id int references critic(id),
  outlet_id int references outlet(id),
  summary text,
  score int,
  recommended boolean,
  url text
);

create table raw_review (
  id serial primary key,
  game text,
  critic text,
  outlet text,
  recommended boolean default false,
  score int,
  summary text,
  title text,
  content text,
  date text,
  url text,
  processed boolean default false
);

-- This is plural, because "user" is a reserved keyword
create table users (
    id serial primary key,
    username text,
    password text,
    role text
);
