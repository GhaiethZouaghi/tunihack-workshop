package tunihack.cognira.utils

/**
  * Config class to use for connecting to Cassandra database
  * Config is populated from file $(promo_planning_root_folder)/script/env.conf
  */
class Config {
  var DB_HOST = "cassandra"
  var DB_PORT = 9042
  var DB_USER = "dse"
  var DB_PASS = ""
}
