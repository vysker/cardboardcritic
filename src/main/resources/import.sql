insert into game (
	name, short_description, description, designer, publisher, release_date, average, median, recommended, slug)
	values
	('Anachrony', 'Not long description', 'Elaborate description', 'David Turczi', 'Mindclash Games', '2017-01-01', 0, 0, 0, 'anachrony'),
	('Gloomhaven', 'Very brief', 'Quite long', 'Isaac Childres', 'Cephalofair Games', '2017-01-01', 0, 0, 0, 'gloomhaven'),
	('Some Game', 'Walk the talk', 'Longer description', 'Nother Dee Signer', 'Big Corp Ltd.', '2022-01-01', 0, 0, 0, 'some-game'),
	('Dune: Imperium - Rise of Ix', 'Dunes, dunes, dunes', 'Sand, nothing but sand', 'Paul Dennen', 'Dire Wolf Digital', '2022-03-24', 0, 0, 0, 'dune-imperium-rise-of-ix'),
	('The game we have been waiting for since forever now',
	 'This description is rather long if I do say so myself, lorem ipum to the dolor sit of amet. And then some, just in case it is not long enough yet',
	 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum consequat hendrerit velit, ut tristique libero sagittis sed. Nulla eget sem magna. Nulla convallis lorem lorem, eget ultricies mauris ornare euismod.',
	 'Nother Dee Signer', 'Slow Co.', '2022-01-01', 0, 0, 0, 'the-game-we-have-been-waiting-for-since-forever-now')
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
  ('Boardgame Scorers', 'https://example.com'),
  ('Cardboard Reviewers', 'https://example.com')
  ;

insert into review (
  game_id, critic_id, outlet_id, summary, score, recommended, published, url)
  values
  (1, 1, 1, 'Anachrony is way ahead of its time', 95, true, true, null),
  (1, 2, 1, 'Born before its time, is what Anachrony is', 90, true, true, null),
  (1, 3, 2, 'Time and time again, I disliked Anachrony', 50, false, true, null),
  (2, 2, 1, 'Although gloomy, it is a haven for fun times', 90, true, true, 'https://arstechnica.com/gaming/2017/04/gloomhaven-review-2017s-biggest-board-game-is-astoundingly-good'),
  (3, 3, 2, 'Now that is some game, I tell you', 70, false, true, null),
  (4, 3, 2, 'There is dunes, and there is Dune', 92, true, true, null),
  (4, 1, 1, 'Sand? Yes. Dunes? Yes. Beach? Nah', 60, false, true, null),
  (4, 1, 2, 'I do not want to see this review published', 80, false, false, null),
  (5, 1, 2, 'I still remember waiting for this game', 40, false, true, null),
  (5, 2, 2, 'https://www.youtube.com/embed/dQw4w9WgXcQ', 40, false, true, null),
  (5, 3, 2, null, 40, false, true, null)
;

insert into raw_review (
  game, critic, outlet, recommended, score, summary, title, content, date, url, processed)
  values
  ('Abc', 'Herb Bergerstern', 'Cardboard Reviewers', false, 0, null, 'We like Anachrony', 'Lorem ipsum', '2017-01-01', null, false),
  ('Anachrony', 'John Doe', 'Boardgame Scorers', false, 0, null, 'It is a game alright', 'Lorem ipsum', '2021-05-29', null, false),
  ('Gloomhaven', 'John Doe', 'Boardgame Scorers', false, 0, null, 'This is the game', 'Lorem ipsum', '2022-06-15', null, false)
  ;

-- Password hashes can be generated with UserRepositoryTest
insert into users (
  username, password, role)
  values
  ('admin', '$2a$10$YlTxbCDSZ1ThYC7kS0Dqb.CCKi2i08euy8Ltl/tOpoKMt23kyeQyO', 'admin') -- Password: admin
  ;
