create index if not exists game_name_ts_idx on game using gin (to_tsvector('english', name));
