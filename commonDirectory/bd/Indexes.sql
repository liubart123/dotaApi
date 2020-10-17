create unique index heroes_roles_index on HeroesRoles(hero_id, role_id);
--drop index heroes_roles_index;
create unique index players_matches_id_index on PlayersMatches(player_id,match_id);
--drop index players_matches_id_index;

--create unique index player_match_stat_index on PlayerMatchTimeStat(player_match_id,time);
--drop index player_match_stat_index;
/
create index bought_items_pim_index on BoughtItems(player_match_id);
create index bought_items_item_index on BoughtItems(item_id);
