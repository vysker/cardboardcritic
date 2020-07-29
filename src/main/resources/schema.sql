create table games (
  id serial primary key,
  name text,
  short_description text,
  description text,
  designer text,
  release_date date,
  score int,
  recommended int
);

create table critics (
  id serial primary key,
  name text
);

create table outlets (
  id serial primary key,
  name text,
  website text
);

create table reviews (
  id serial primary key,
  game_id int references games(id),
  critic_id int references critics(id),
  outlet_id int references outlets(id),
  summary text,
  score int,
  recommended boolean,
  link text
);
