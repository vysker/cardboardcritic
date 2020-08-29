insert into game (
	id, name, short_description, description, designer, release_date, score, recommended, slug)
	values
	(1, 'Anachrony', 'Not long description', 'Elaborate description', 'David Turczi', '2017-01-01', 80, 70, 'anachrony'),
	(2, 'Gloomhaven', 'Very brief', 'Quite long', 'Isaac Childres', '2017-01-01', 89, 80, 'gloomhaven')
	;

insert into critic (
  id, name)
  values
  (1, 'John Doe'),
  (2, 'Jane Bane'),
  (3, 'Heckler Barrymore')
  ;

insert into outlet (
  id, name, website)
  values
  (1, 'Ars Technica', 'https://arstechnica.com/'),
  (2, 'Eurogamer', 'https://eurogamer.net')
  ;

insert into review (
  id, game_id, critic_id, outlet_id, summary, score, recommended, url)
  values
  (1, 1, 1, 1, 'Lorem ipsum dolor sit amet', 95, true, 'https://arstechnica.com/gaming/2017/04/gloomhaven-review-2017s-biggest-board-game-is-astoundingly-good'),
  (2, 1, 2, 1, 'Lorem ipsum dolor sit amet', 90, true, null),
  (3, 1, 3, 2, 'Lorem ipsum dolor sit amet', 50, false, null)
  ;

insert into raw_review (
  id, game, critic, outlet, recommended, score, summary, title, content, date, url, processed)
  values
  (1, 'Abc', 'Herb Bergerstern', null, false, 0, null, 'We like Anachrony', 'Lorem ipsum', '2017-01-01', null, false)
  ;
