read -r -d '' VCAP_APPLICATION <<'ENDOFVAR'
{"application_version":"1","application_name":"sample-client","application_uris":[""],"version":"1.0","name":"sample-client","instance_id":"abcd","instance_index":0}
ENDOFVAR

export VCAP_APPLICATION=$VCAP_APPLICATION

read -r -d '' VCAP_SERVICES <<'ENDOFVAR'
{
  "p-identity": [
    {
      "credentials": {
        "client_id": "live-test-auth",
        "client_secret": "live-test-auth",
        "auth_domain": "http://localhost:8080"
      },
      "syslog_drain_url": null,
      "volume_mounts": [],
      "label": "p-identity",
      "provider": null,
      "plan": "uaa-only",
      "name": "bk-test-sso",
      "tags": []
    }
  ]
}
ENDOFVAR

export VCAP_SERVICES=$VCAP_SERVICES

./gradlew clean bootRun