insert into game (
	name, short_description, description, designer, release_date, score, recommended, slug)
	values
	('Anachrony', 'Not long description', 'Elaborate description', 'David Turczi', '2017-01-01', 80, 70, 'anachrony'),
	('Gloomhaven', 'Very brief', 'Quite long', 'Isaac Childres', '2017-01-01', 89, 80, 'gloomhaven'),
	('Some Game', 'Walk the talk', 'Longer description', 'Nother Dee Signer', '2022-01-01', 85, 80, 'some-game'),
	('The game we have been waiting for since forever now', 'This is the game', 'Epic to the max', 'Nother Dee Signer', '2022-01-01', 85, 80, 'the-game-we-have-been-waiting-for-since-forever-now')
	;

insert into critic (
  name)
  values
  ('John Doe'),
  ('Jane Bane'),
  ('Heckler Barrymore')
  ;

insert into outlet (
  name, website)
  values
  ('Ars Technica', 'https://arstechnica.com/'),
  ('Eurogamer', 'https://eurogamer.net')
  ;

insert into review (
  game_id, critic_id, outlet_id, summary, score, recommended, url)
  values
  (1, 1, 1, 'Lorem ipsum dolor sit amet', 95, true, 'https://arstechnica.com/gaming/2017/04/gloomhaven-review-2017s-biggest-board-game-is-astoundingly-good'),
  (1, 2, 1, 'Lorem ipsum dolor sit amet', 90, true, null),
  (1, 3, 2, 'Lorem ipsum dolor sit amet', 50, false, null),
  (3, 3, 2, 'Lorem ipsum dolor sit amet', 50, false, null)
  ;

insert into raw_review (
  game, critic, outlet, recommended, score, summary, title, content, date, url, processed)
  values
  ('Abc', 'Herb Bergerstern', null, false, 0, null, 'We like Anachrony', 'Lorem ipsum', '2017-01-01', null, false)
  ;
