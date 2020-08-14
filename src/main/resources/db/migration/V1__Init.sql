create table game (
  id serial primary key,
  name text,
  short_description text,
  description text,
  designer text,
  release_date date,
  score int,
  recommended int
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
  link text
);
