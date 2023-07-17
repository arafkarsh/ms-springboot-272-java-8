--
-- PostgreSQL database dump
--

-- Dumped from database version 14.4
-- Dumped by pg_dump version 14.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: ms_schema; Type: SCHEMA; Schema: -; Owner: arafkarsh
--

CREATE SCHEMA ms_schema;


ALTER SCHEMA ms_schema OWNER TO arafkarsh;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: carts_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.carts_tx (
    uuid bpchar NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp(6) without time zone NOT NULL,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp(6) without time zone NOT NULL,
    isactive boolean,
    version integer,
    customerid character varying(255) NOT NULL,
    productid character varying(255) NOT NULL,
    productname character varying(32),
    price numeric(38,2) NOT NULL,
    quantity numeric(38,2) NOT NULL
);


ALTER TABLE ms_schema.carts_tx OWNER TO postgres;

--
-- Name: country_geolite_m; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.country_geolite_m (
    geoname_id integer NOT NULL,
    locale_code character varying NOT NULL,
    continent_code character varying NOT NULL,
    continent_name character varying NOT NULL,
    country_iso_code character varying,
    country_name character varying,
    is_in_european_union integer NOT NULL
);


ALTER TABLE ms_schema.country_geolite_m OWNER TO postgres;

--
-- Name: country_t; Type: TABLE; Schema: ms_schema; Owner: arafkarsh
--

CREATE TABLE ms_schema.country_t (
    cid integer NOT NULL,
    countryid integer NOT NULL,
    countrycode character varying(255) NOT NULL,
    countryname character varying(255) NOT NULL,
    countryofficialname character varying(255)
);


ALTER TABLE ms_schema.country_t OWNER TO arafkarsh;

--
-- Name: order_item_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.order_item_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    price numeric(19,2),
    productid character varying(255),
    productname character varying(255),
    quantity numeric(19,2),
    order_id character(36)
);


ALTER TABLE ms_schema.order_item_tx OWNER TO postgres;

--
-- Name: order_payment_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.order_payment_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    status character varying(255),
    transaction_id character varying(255)
);


ALTER TABLE ms_schema.order_payment_tx OWNER TO postgres;

--
-- Name: order_state_history_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.order_state_history_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    notes character varying(255),
    sourcestate character varying(255),
    targetstate character varying(255),
    transitionevent character varying(255),
    order_id character(36),
    orderversion integer
);


ALTER TABLE ms_schema.order_state_history_tx OWNER TO postgres;

--
-- Name: order_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.order_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    currency character varying(255),
    customer_id character varying(255),
    city character varying(255),
    country character varying(255),
    landmark character varying(255),
    phone character varying(255),
    state character varying(255),
    street character varying(255),
    zip_code character varying(255),
    totalordervalue numeric(19,2),
    payment_id character(36),
    orderstatus character varying(255),
    result character varying(255)
);


ALTER TABLE ms_schema.order_tx OWNER TO postgres;

--
-- Name: products_m; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.products_m (
    uuid bpchar NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp(6) without time zone NOT NULL,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp(6) without time zone NOT NULL,
    isactive boolean,
    version integer,
    productdetails character varying(64) NOT NULL,
    productlocationzipcode character varying(255),
    productname character varying(32),
    price numeric(38,2) NOT NULL
);


ALTER TABLE ms_schema.products_m OWNER TO postgres;

--
-- Name: reservation_flight_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.reservation_flight_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    airlines character varying(255),
    flightid character varying(255),
    flightreservationno character varying(255),
    fromcity character varying(255),
    journeydate date,
    pnr character varying(255),
    rate integer,
    reservationstatus character varying(255),
    statusreasons character varying(255),
    tocity character varying(255),
    reservation_id character(36),
    gender character varying(255),
    passengername character varying(255)
);


ALTER TABLE ms_schema.reservation_flight_tx OWNER TO postgres;

--
-- Name: reservation_hotel_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.reservation_hotel_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    days integer,
    enddate date,
    hotelid character varying(255),
    hotelname character varying(255),
    hotelreservationno character varying(255),
    persons integer,
    rate integer,
    reservationstatus character varying(255),
    startdate date,
    reservation_id character(36)
);


ALTER TABLE ms_schema.reservation_hotel_tx OWNER TO postgres;

--
-- Name: reservation_payment_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.reservation_payment_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    status character varying(255),
    transaction_id character varying(255)
);


ALTER TABLE ms_schema.reservation_payment_tx OWNER TO postgres;

--
-- Name: reservation_rental_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.reservation_rental_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    days integer,
    enddate date,
    rate integer,
    rentalid character varying(255),
    rentalname character varying(255),
    rentalreservationno character varying(255),
    rentaltype character varying(255),
    rentalvehiclelicense character varying(255),
    reservationstatus character varying(255),
    startdate date,
    statusreasons character varying(255),
    reservation_id character(36),
    primarydriver character varying(255)
);


ALTER TABLE ms_schema.reservation_rental_tx OWNER TO postgres;

--
-- Name: reservation_state_history_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.reservation_state_history_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    aggregateversionno integer DEFAULT 0,
    notes character varying(255),
    sourcestate character varying(255),
    targetstate character varying(255),
    transitionevent character varying(255),
    reservation_id character(36)
);


ALTER TABLE ms_schema.reservation_state_history_tx OWNER TO postgres;

--
-- Name: reservation_tx; Type: TABLE; Schema: ms_schema; Owner: postgres
--

CREATE TABLE ms_schema.reservation_tx (
    uuid character(36) NOT NULL,
    createdby character varying(255) NOT NULL,
    createdtime timestamp without time zone NOT NULL,
    isactive boolean,
    updatedby character varying(255) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    version integer,
    currency character varying(255),
    city character varying(255),
    country character varying(255),
    landmark character varying(255),
    phone character varying(255),
    state character varying(255),
    street character varying(255),
    zip_code character varying(255),
    customer_id character varying(255),
    reservationstatus character varying(255),
    result character varying(255),
    totalvalue integer,
    payment_id character(36),
    rollbackonfailure boolean
);


ALTER TABLE ms_schema.reservation_tx OWNER TO postgres;

--
-- Data for Name: carts_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.carts_tx (uuid, createdby, createdtime, updatedby, updatedtime, isactive, version, customerid, productid, productname, price, quantity) FROM stdin;
22273a09-ee9e-4e5a-98fa-6bcfcfa97b49	john.doe	2023-05-28 20:26:12.919	john.doe	2023-05-28 20:26:12.919	t	0	123	789	Pencil	10.00	3.00
7b54e398-711a-4820-a32c-81c7dfab1ab1	john.doe	2023-05-28 20:26:12.919	john.doe	2023-05-28 20:26:12.919	t	0	123	678	Pen	30.00	2.00
b1edfc2d-a907-4806-b51f-28c7bd3cdd3f	john.doe	2023-05-28 20:26:12.919	john.doe	2023-05-28 20:26:12.919	t	0	123	902	Paper 100 Bundle	50.00	5.00
bbef5710-e7ba-4bc9-9c01-353dbd82dd6e	john.doe	2023-05-28 20:26:12.919	john.doe	2023-05-28 20:26:12.919	t	0	123	902	Book Lined	30.00	3.00
a43e9fd2-a3bd-4281-91e9-96f5e130f032	anonymousUser	2023-07-02 10:30:12.991269	anonymousUser	2023-07-02 10:30:12.991269	t	0	123	1542	Lunch Box	35.00	1.00
faca053a-b4af-4a31-aa15-bf2dbf2102e1	anonymousUser	2023-07-02 10:30:53.952645	anonymousUser	2023-07-02 10:30:53.952645	t	0	123	1543	School Bag	45.00	1.00
f9e9c861-e48c-4485-ac7d-5f6c29beeb37	anonymousUser	2023-07-02 10:35:50.113237	anonymousUser	2023-07-02 10:35:50.113237	t	0	123	1544	Umbrella	32.00	1.00
\.


--
-- Data for Name: country_geolite_m; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.country_geolite_m (geoname_id, locale_code, continent_code, continent_name, country_iso_code, country_name, is_in_european_union) FROM stdin;
49518	en	AF	Africa	RW	Rwanda	0
51537	en	AF	Africa	SO	Somalia	0
69543	en	AS	Asia	YE	Yemen	0
99237	en	AS	Asia	IQ	Iraq	0
102358	en	AS	Asia	SA	Saudi Arabia	0
130758	en	AS	Asia	IR	Iran	0
146669	en	EU	Europe	CY	Cyprus	1
149590	en	AF	Africa	TZ	Tanzania	0
163843	en	AS	Asia	SY	Syria	0
174982	en	AS	Asia	AM	Armenia	0
192950	en	AF	Africa	KE	Kenya	0
203312	en	AF	Africa	CD	DR Congo	0
223816	en	AF	Africa	DJ	Djibouti	0
226074	en	AF	Africa	UG	Uganda	0
239880	en	AF	Africa	CF	Central African Republic	0
241170	en	AF	Africa	SC	Seychelles	0
248816	en	AS	Asia	JO	Jordan	0
272103	en	AS	Asia	LB	Lebanon	0
285570	en	AS	Asia	KW	Kuwait	0
286963	en	AS	Asia	OM	Oman	0
289688	en	AS	Asia	QA	Qatar	0
290291	en	AS	Asia	BH	Bahrain	0
290557	en	AS	Asia	AE	United Arab Emirates	0
294640	en	AS	Asia	IL	Israel	0
298795	en	AS	Asia	TR	Turkey	0
337996	en	AF	Africa	ET	Ethiopia	0
338010	en	AF	Africa	ER	Eritrea	0
357994	en	AF	Africa	EG	Egypt	0
366755	en	AF	Africa	SD	Sudan	0
390903	en	EU	Europe	GR	Greece	1
433561	en	AF	Africa	BI	Burundi	0
453733	en	EU	Europe	EE	Estonia	1
458258	en	EU	Europe	LV	Latvia	1
587116	en	AS	Asia	AZ	Azerbaijan	0
597427	en	EU	Europe	LT	Lithuania	1
607072	en	EU	Europe	SJ	Svalbard and Jan Mayen	0
614540	en	AS	Asia	GE	Georgia	0
617790	en	EU	Europe	MD	Moldova	0
630336	en	EU	Europe	BY	Belarus	0
660013	en	EU	Europe	FI	Finland	1
661882	en	EU	Europe	AX	Åland Islands	1
690791	en	EU	Europe	UA	Ukraine	0
718075	en	EU	Europe	MK	North Macedonia	0
719819	en	EU	Europe	HU	Hungary	1
732800	en	EU	Europe	BG	Bulgaria	1
783754	en	EU	Europe	AL	Albania	0
798544	en	EU	Europe	PL	Poland	1
798549	en	EU	Europe	RO	Romania	1
831053	en	EU	Europe	XK	Kosovo	0
878675	en	AF	Africa	ZW	Zimbabwe	0
895949	en	AF	Africa	ZM	Zambia	0
921929	en	AF	Africa	KM	Comoros	0
927384	en	AF	Africa	MW	Malawi	0
932692	en	AF	Africa	LS	Lesotho	0
933860	en	AF	Africa	BW	Botswana	0
934292	en	AF	Africa	MU	Mauritius	0
934841	en	AF	Africa	SZ	Eswatini	0
935317	en	AF	Africa	RE	Réunion	1
953987	en	AF	Africa	ZA	South Africa	0
1024031	en	AF	Africa	YT	Mayotte	1
1036973	en	AF	Africa	MZ	Mozambique	0
1062947	en	AF	Africa	MG	Madagascar	0
1149361	en	AS	Asia	AF	Afghanistan	0
1168579	en	AS	Asia	PK	Pakistan	0
1210997	en	AS	Asia	BD	Bangladesh	0
1218197	en	AS	Asia	TM	Turkmenistan	0
1220409	en	AS	Asia	TJ	Tajikistan	0
1227603	en	AS	Asia	LK	Sri Lanka	0
1252634	en	AS	Asia	BT	Bhutan	0
1269750	en	AS	Asia	IN	India	0
1282028	en	AS	Asia	MV	Maldives	0
1282588	en	AS	Asia	IO	British Indian Ocean Territory	0
1282988	en	AS	Asia	NP	Nepal	0
1327865	en	AS	Asia	MM	Myanmar	0
1512440	en	AS	Asia	UZ	Uzbekistan	0
1522867	en	AS	Asia	KZ	Kazakhstan	0
1527747	en	AS	Asia	KG	Kyrgyzstan	0
1546748	en	AN	Antarctica	TF	French Southern Territories	0
1547314	en	AN	Antarctica	HM	Heard and McDonald Islands	0
1547376	en	AS	Asia	CC	Cocos (Keeling) Islands	0
1559582	en	OC	Oceania	PW	Palau	0
1562822	en	AS	Asia	VN	Vietnam	0
1605651	en	AS	Asia	TH	Thailand	0
1643084	en	AS	Asia	ID	Indonesia	0
1655842	en	AS	Asia	LA	Laos	0
1668284	en	AS	Asia	TW	Taiwan	0
1694008	en	AS	Asia	PH	Philippines	0
1733045	en	AS	Asia	MY	Malaysia	0
1814991	en	AS	Asia	CN	China	0
1819730	en	AS	Asia	HK	Hong Kong	0
1820814	en	AS	Asia	BN	Brunei	0
1821275	en	AS	Asia	MO	Macao	0
1831722	en	AS	Asia	KH	Cambodia	0
1835841	en	AS	Asia	KR	South Korea	0
1861060	en	AS	Asia	JP	Japan	0
1873107	en	AS	Asia	KP	North Korea	0
1880251	en	AS	Asia	SG	Singapore	0
1899402	en	OC	Oceania	CK	Cook Islands	0
1966436	en	OC	Oceania	TL	Timor-Leste	0
2017370	en	EU	Europe	RU	Russia	0
2029969	en	AS	Asia	MN	Mongolia	0
2077456	en	OC	Oceania	AU	Australia	0
2078138	en	OC	Oceania	CX	Christmas Island	0
2080185	en	OC	Oceania	MH	Marshall Islands	0
2081918	en	OC	Oceania	FM	Federated States of Micronesia	0
2088628	en	OC	Oceania	PG	Papua New Guinea	0
2103350	en	OC	Oceania	SB	Solomon Islands	0
2110297	en	OC	Oceania	TV	Tuvalu	0
2110425	en	OC	Oceania	NR	Nauru	0
2134431	en	OC	Oceania	VU	Vanuatu	0
2139685	en	OC	Oceania	NC	New Caledonia	0
2155115	en	OC	Oceania	NF	Norfolk Island	0
2186224	en	OC	Oceania	NZ	New Zealand	0
2205218	en	OC	Oceania	FJ	Fiji	0
2215636	en	AF	Africa	LY	Libya	0
2233387	en	AF	Africa	CM	Cameroon	0
2245662	en	AF	Africa	SN	Senegal	0
2260494	en	AF	Africa	CG	Congo Republic	0
2264397	en	EU	Europe	PT	Portugal	1
2275384	en	AF	Africa	LR	Liberia	0
2287781	en	AF	Africa	CI	Ivory Coast	0
2300660	en	AF	Africa	GH	Ghana	0
2309096	en	AF	Africa	GQ	Equatorial Guinea	0
2328926	en	AF	Africa	NG	Nigeria	0
2361809	en	AF	Africa	BF	Burkina Faso	0
2363686	en	AF	Africa	TG	Togo	0
2372248	en	AF	Africa	GW	Guinea-Bissau	0
2378080	en	AF	Africa	MR	Mauritania	0
2395170	en	AF	Africa	BJ	Benin	0
2400553	en	AF	Africa	GA	Gabon	0
2403846	en	AF	Africa	SL	Sierra Leone	0
2410758	en	AF	Africa	ST	São Tomé and Príncipe	0
2411586	en	EU	Europe	GI	Gibraltar	0
2413451	en	AF	Africa	GM	Gambia	0
2420477	en	AF	Africa	GN	Guinea	0
2434508	en	AF	Africa	TD	Chad	0
2440476	en	AF	Africa	NE	Niger	0
2453866	en	AF	Africa	ML	Mali	0
2461445	en	AF	Africa	EH	Western Sahara	0
2464461	en	AF	Africa	TN	Tunisia	0
2510769	en	EU	Europe	ES	Spain	1
2542007	en	AF	Africa	MA	Morocco	0
2562770	en	EU	Europe	MT	Malta	1
2589581	en	AF	Africa	DZ	Algeria	0
2622320	en	EU	Europe	FO	Faroe Islands	0
2623032	en	EU	Europe	DK	Denmark	1
2629691	en	EU	Europe	IS	Iceland	0
2635167	en	EU	Europe	GB	United Kingdom	0
2658434	en	EU	Europe	CH	Switzerland	0
2661886	en	EU	Europe	SE	Sweden	1
2750405	en	EU	Europe	NL	Netherlands	1
2782113	en	EU	Europe	AT	Austria	1
2802361	en	EU	Europe	BE	Belgium	1
2921044	en	EU	Europe	DE	Germany	1
2960313	en	EU	Europe	LU	Luxembourg	1
2963597	en	EU	Europe	IE	Ireland	1
2993457	en	EU	Europe	MC	Monaco	0
3017382	en	EU	Europe	FR	France	1
3041565	en	EU	Europe	AD	Andorra	0
3042058	en	EU	Europe	LI	Liechtenstein	0
3042142	en	EU	Europe	JE	Jersey	0
3042225	en	EU	Europe	IM	Isle of Man	0
3042362	en	EU	Europe	GG	Guernsey	0
3057568	en	EU	Europe	SK	Slovakia	1
3077311	en	EU	Europe	CZ	Czechia	1
3144096	en	EU	Europe	NO	Norway	0
3164670	en	EU	Europe	VA	Vatican City	0
3168068	en	EU	Europe	SM	San Marino	0
3175395	en	EU	Europe	IT	Italy	1
3190538	en	EU	Europe	SI	Slovenia	1
3194884	en	EU	Europe	ME	Montenegro	0
3202326	en	EU	Europe	HR	Croatia	1
3277605	en	EU	Europe	BA	Bosnia and Herzegovina	0
3351879	en	AF	Africa	AO	Angola	0
3355338	en	AF	Africa	NA	Namibia	0
3370751	en	AF	Africa	SH	Saint Helena	0
3371123	en	AN	Antarctica	BV	Bouvet Island	0
3374084	en	NA	North America	BB	Barbados	0
3374766	en	AF	Africa	CV	Cabo Verde	0
3378535	en	SA	South America	GY	Guyana	0
3381670	en	SA	South America	GF	French Guiana	1
3382998	en	SA	South America	SR	Suriname	0
3424932	en	NA	North America	PM	Saint Pierre and Miquelon	0
3425505	en	NA	North America	GL	Greenland	0
3437598	en	SA	South America	PY	Paraguay	0
3439705	en	SA	South America	UY	Uruguay	0
3469034	en	SA	South America	BR	Brazil	0
3474414	en	SA	South America	FK	Falkland Islands	0
3474415	en	AN	Antarctica	GS	South Georgia and the South Sandwich Islands	0
3489940	en	NA	North America	JM	Jamaica	0
3508796	en	NA	North America	DO	Dominican Republic	0
3562981	en	NA	North America	CU	Cuba	0
3570311	en	NA	North America	MQ	Martinique	1
3572887	en	NA	North America	BS	Bahamas	0
3573345	en	NA	North America	BM	Bermuda	0
3573511	en	NA	North America	AI	Anguilla	0
3573591	en	NA	North America	TT	Trinidad and Tobago	0
3575174	en	NA	North America	KN	St Kitts and Nevis	0
3575830	en	NA	North America	DM	Dominica	0
3576396	en	NA	North America	AG	Antigua and Barbuda	0
3576468	en	NA	North America	LC	Saint Lucia	0
3576916	en	NA	North America	TC	Turks and Caicos Islands	0
3577279	en	NA	North America	AW	Aruba	0
3577718	en	NA	North America	VG	British Virgin Islands	0
3577815	en	NA	North America	VC	St Vincent and Grenadines	0
3578097	en	NA	North America	MS	Montserrat	0
3578421	en	NA	North America	MF	Saint Martin	1
3578476	en	NA	North America	BL	Saint Barthélemy	0
3579143	en	NA	North America	GP	Guadeloupe	1
3580239	en	NA	North America	GD	Grenada	0
3580718	en	NA	North America	KY	Cayman Islands	0
3582678	en	NA	North America	BZ	Belize	0
3585968	en	NA	North America	SV	El Salvador	0
3595528	en	NA	North America	GT	Guatemala	0
3608932	en	NA	North America	HN	Honduras	0
3617476	en	NA	North America	NI	Nicaragua	0
3624060	en	NA	North America	CR	Costa Rica	0
3625428	en	SA	South America	VE	Venezuela	0
3658394	en	SA	South America	EC	Ecuador	0
3686110	en	SA	South America	CO	Colombia	0
3703430	en	NA	North America	PA	Panama	0
3723988	en	NA	North America	HT	Haiti	0
3865483	en	SA	South America	AR	Argentina	0
3895114	en	SA	South America	CL	Chile	0
3923057	en	SA	South America	BO	Bolivia	0
3932488	en	SA	South America	PE	Peru	0
3996063	en	NA	North America	MX	Mexico	0
4030656	en	OC	Oceania	PF	French Polynesia	0
4030699	en	OC	Oceania	PN	Pitcairn Islands	0
4030945	en	OC	Oceania	KI	Kiribati	0
4031074	en	OC	Oceania	TK	Tokelau	0
4032283	en	OC	Oceania	TO	Tonga	0
4034749	en	OC	Oceania	WF	Wallis and Futuna	0
4034894	en	OC	Oceania	WS	Samoa	0
4036232	en	OC	Oceania	NU	Niue	0
4041468	en	OC	Oceania	MP	Northern Mariana Islands	0
4043988	en	OC	Oceania	GU	Guam	0
4566966	en	NA	North America	PR	Puerto Rico	0
4796775	en	NA	North America	VI	U.S. Virgin Islands	0
5854968	en	OC	Oceania	UM	U.S. Outlying Islands	0
5880801	en	OC	Oceania	AS	American Samoa	0
6251999	en	NA	North America	CA	Canada	0
6252001	en	NA	North America	US	United States	0
6254930	en	AS	Asia	PS	Palestine	0
6255147	en	AS	Asia	\N	\N	0
6255148	en	EU	Europe	\N	\N	0
6290252	en	EU	Europe	RS	Serbia	0
6697173	en	AN	Antarctica	AQ	Antarctica	0
7609695	en	NA	North America	SX	Sint Maarten	0
7626836	en	NA	North America	CW	Curaçao	0
7626844	en	NA	North America	BQ	Bonaire, Sint Eustatius, and Saba	0
7909807	en	AF	Africa	SS	South Sudan	0
\.


--
-- Data for Name: country_t; Type: TABLE DATA; Schema: ms_schema; Owner: arafkarsh
--

COPY ms_schema.country_t (cid, countryid, countrycode, countryname, countryofficialname) FROM stdin;
1	1	USA	America	United States of America
2	250	FRA	France	The French Republic
3	76	BRA	Brazil	The Federative Republic of Brazil
6	124	CAN	Canada	Canada
7	276	DEU	Germany	Federal Republic of Germany
8	724	ESP	Spain	Kingdom of Spain
9	380	ITA	Italy	Italian Republic
10	56	BEL	Belgium	Kingdom of Belgium
11	528	NLD	Netherlands	Kingdom of Netherlands
12	616	POL	Poland	Republic of Poland
13	792	TUR	Turkey	Republic of Turkey
15	40	AUT	Austria	Republic of Austria
14	756	CHE	Switzerland	Swiss Confederation
16	32	ARG	Argentina	Argentine Republic
17	76	BRA	Brazil	Federative Republic of Brazil
18	152	CHL	Chile	Republic of Chile
19	170	COL	Columbia	Republic of Columbia
20	484	MEX	Mexico	United Mexican States
21	858	URY	Uruguay	Oriental Republic of Uruguay
22	604	PER	Peru	Republic of Peru
23	862	VEN	Venezuela	Bolivarin Republic of Venezuela
4	111	ITA	Italy	Italy
5	356	IND	India	Republic of India
\.


--
-- Data for Name: order_item_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.order_item_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, price, productid, productname, quantity, order_id) FROM stdin;
fb19e155-144c-493d-895d-ff7614e7f53f	anonymousUser	2023-07-05 11:24:30.548063	t	anonymousUser	2023-07-07 12:44:27.556828	1	40000.00	5dcb87dd-0480-4e45-be83-ddcba3313c4f	Apple Watch 5	1.00	7a8bc3d8-911f-4aee-bc71-5dfa25a4a3ae
34510d2c-663b-40da-9154-55aa3ee75118	anonymousUser	2023-07-05 11:24:30.547118	t	anonymousUser	2023-07-07 12:44:27.556975	1	45000.00	25886064-2a6d-417a-9040-111166f250f3	Apple Watch 6	1.00	7a8bc3d8-911f-4aee-bc71-5dfa25a4a3ae
33afea94-2cde-40e5-afee-0dd4de32546b	anonymousUser	2023-07-07 11:24:30.547	t	anonymousUser	2023-07-07 12:44:27.556975	1	90000.00	25886064-2a6d-417a-9040-111166f250f3	iPhone 15	1.00	e5a5bb0d-6282-4072-9926-a0653095fd07
3bb8be4a-ca61-4d19-9a7d-5e43c134916a	anonymousUser	2023-07-07 11:24:30.547	t	anonymousUser	2023-07-07 12:44:27.556975	1	80000.00	25886064-2a6d-417a-9040-111166f250f3	iPhone 14	1.00	e5a5bb0d-6282-4072-9926-a0653095fd07
\.


--
-- Data for Name: order_payment_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.order_payment_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, status, transaction_id) FROM stdin;
63fd3531-ee6b-40d8-aed0-2faeebc41c87	anonymousUser	2023-07-05 11:24:30.527592	t	anonymousUser	2023-07-05 11:24:30.527592	0	string	string
999bda45-b1ad-4af3-a2fa-4564cca52ed6	anonymousUser	2023-07-07 12:44:27.541352	t	anonymousUser	2023-07-07 12:44:27.541352	0	string	string
\.


--
-- Data for Name: order_state_history_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.order_state_history_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, notes, sourcestate, targetstate, transitionevent, order_id, orderversion) FROM stdin;
c86ddba1-6869-495d-9e44-3d181ea41f87	anonymousUser	2023-07-13 18:00:32.710834	t	anonymousUser	2023-07-13 18:00:32.710834	0		PAYMENT_CONFIRMED	PACKING_FORK	PACKAGE_FORK_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1605
1f33c58e-ae3d-42bc-a5cf-eb622a888603	anonymousUser	2023-07-13 18:00:32.716818	t	anonymousUser	2023-07-13 18:00:32.716818	0		PACKING_FORK	ORDER_PACKAGING_START	PACKAGE_FORK_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1606
f8d069bc-4f15-4c67-8dd1-ce6e87f4f693	anonymousUser	2023-07-13 18:00:32.721738	t	anonymousUser	2023-07-13 18:00:32.721738	0		ORDER_PACKAGING_START	SEND_BILL_START	PACKAGE_FORK_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1607
b0e36d16-d0a2-4f27-9022-81758040d6ba	anonymousUser	2023-07-13 18:00:32.728011	t	anonymousUser	2023-07-13 18:00:32.728011	0		SEND_BILL_START	ORDER_PACKAGING_DONE	PACKAGE_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1608
1f49f434-a161-4a63-a8fc-5bfb7067e826	anonymousUser	2023-07-13 18:00:32.736294	t	anonymousUser	2023-07-13 18:00:32.736294	0		ORDER_PACKAGING_DONE	SEND_BILL_DONE	ORDER_SEND_BILL_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1609
845665d0-9019-473b-8a97-1975961c67c7	anonymousUser	2023-07-13 18:00:32.744518	t	anonymousUser	2023-07-13 18:00:32.744518	0		PACKING_FORK	SHIPPED	ORDER_SEND_BILL_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1610
83cf194e-84b8-45b7-a591-2d2d422de703	anonymousUser	2023-07-13 18:00:33.835162	t	anonymousUser	2023-07-13 18:00:33.835162	0		SHIPPED	IN_TRANSIT	ORDER_IN_TRANSIT_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1611
016cc36f-0e57-48de-bca4-01d74941dfe3	anonymousUser	2023-07-13 18:00:34.888597	t	anonymousUser	2023-07-13 18:00:34.888597	0		IN_TRANSIT	REACHED_DESTINATION	SEND_FOR_DELIVERY_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1612
6ddd6cd3-26d7-43e2-bb4d-687a6e9ec314	anonymousUser	2023-07-13 18:00:35.968272	t	anonymousUser	2023-07-13 18:00:35.968272	0		REACHED_DESTINATION	DELIVERED	ORDER_DELIVERED_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1613
67883839-bbb7-453c-bf72-b0ffc8424ef9	anonymousUser	2023-07-13 18:00:35.976364	t	anonymousUser	2023-07-13 18:00:35.976364	0		DELIVERED	ORDER_COMPLETED	AUTO_TRANSITION_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1614
dad9efb3-dc2b-4e83-afff-6ad2ecb638a8	anonymousUser	2023-07-13 18:00:28.454872	t	anonymousUser	2023-07-13 18:00:28.454872	0		ORDER_INITIALIZED	CREDIT_CHECKING	CREDIT_CHECKING_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1601
ba4341e4-40db-4628-bd8b-4af8d079ccd4	anonymousUser	2023-07-13 18:00:29.539638	t	anonymousUser	2023-07-13 18:00:29.539638	0		CREDIT_CHECKING	CREDIT_APPROVED	CREDIT_APPROVED_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1602
94420e1c-63ec-418f-9d93-6e0fe20b714c	anonymousUser	2023-07-13 18:00:30.598671	t	anonymousUser	2023-07-13 18:00:30.598671	0		CREDIT_APPROVED	PAYMENT_PROCESSING	PAYMENT_INIT_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1603
37d17669-8bf6-4bfd-acef-ac28bb5d8749	anonymousUser	2023-07-13 18:00:31.657143	t	anonymousUser	2023-07-13 18:00:31.657143	0		PAYMENT_PROCESSING	PAYMENT_CONFIRMED	PAYMENT_APPROVED_EVENT	e5a5bb0d-6282-4072-9926-a0653095fd07	1604
\.


--
-- Data for Name: order_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.order_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, currency, customer_id, city, country, landmark, phone, state, street, zip_code, totalordervalue, payment_id, orderstatus, result) FROM stdin;
7a8bc3d8-911f-4aee-bc71-5dfa25a4a3ae	anonymousUser	2023-07-07 12:44:27.546798	t	anonymousUser	2023-07-11 11:43:12.021425	40	INR	123	Edison	USA	Near Walmart	7321002010	NJ	321 Cobblestone Lane	08110	85000.00	999bda45-b1ad-4af3-a2fa-4564cca52ed6	ORDER_INITIALIZED	IN_PROGRESS
e5a5bb0d-6282-4072-9926-a0653095fd07	anonymousUser	2023-07-05 11:24:30.537602	t	anonymousUser	2023-07-13 18:00:35.97734	1614	INR	123	Edison	USA	Near Walmart	7321002010	NJ	321 Cobblestone Lan	08110	170000.00	63fd3531-ee6b-40d8-aed0-2faeebc41c87	ORDER_COMPLETED	DELIVERED
\.


--
-- Data for Name: products_m; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.products_m (uuid, createdby, createdtime, updatedby, updatedtime, isactive, version, productdetails, productlocationzipcode, productname, price) FROM stdin;
7b54e398-711a-4820-a32c-81c7dfab1ab1	john.doe	2023-05-28 20:26:12.925	john.doe	2023-05-28 20:26:12.925	t	0	iPhone 11, 128 GB	12345	iPhone 11	70000.00
14879f35-9b10-4b75-8641-280731161aee	john.doe	2023-05-28 20:26:12.925	john.doe	2023-05-28 20:26:12.925	t	0	Samsung Galaxy s20, 256 GB	12345	Samsung Galaxy s20	80000.00
2b4d83d6-0063-429e-9d27-94aacb6abba6	john.doe	2023-05-28 20:31:14.267	john.doe	2023-05-28 20:31:14.267	t	0	Google Pixel 7, 128 GB SSD, 16GB RAM	98321	Google Pixel 7	60000.00
273c4812-40cb-4939-ae22-f4ad89d7a0ec	john.doe	2023-05-28 20:32:40.006	john.doe	2023-05-28 20:32:40.006	t	0	Samsung Galaxy S22, 16GB RAM, 128 GB SDD	46123	Samsung Galaxy S22	65000.00
eef67186-ff2a-42b9-809e-93536d0c1076	john.doe	2023-05-28 23:11:41.662	john.doe	2023-05-28 23:11:41.662	t	0	Google Pixel 6, 8GB RAM, 64GB SSD	12345	Google Pixel 6	50000.00
1a9d1e4e-525b-4dc4-b8ad-9a354a7b3418	john.doe	2023-06-25 19:21:01.598	john.doe	2023-06-25 19:21:01.598	t	0	Google Pixel 5 32 GB	12345	Google Pixel 5	35000.00
434ca819-4d11-4965-a88a-c087681414a8	john.doe	2023-06-25 22:26:09.847	john.doe	2023-06-25 22:26:09.847	t	0	Google Pixel 5 32 GB	12345	Google Pixel 5	35000.00
6c4bd74b-fb0a-4988-b83d-531744e4f8bf	john.doe	2023-06-25 22:26:21.592	john.doe	2023-06-25 22:26:21.592	t	0	Google Pixel 5 64 GB	12345	Google Pixel 5	40000.00
fd3288ef-2d49-4ea1-9a58-bb2f280fc2bd	john.doe	2023-06-25 22:44:10.901	john.doe	2023-06-25 22:44:10.901	t	0	Google Pixel 5 128 GB	12345	Google Pixel 5	50000.00
75383383-8ed7-4c28-a79c-7ca1cd6713e8	john.doe	2023-06-25 23:05:11.667	john.doe	2023-06-25 23:05:11.667	t	0	iPhone 10, 128GB BLACK	12345	iPhone 10	65000.00
adff3e12-d019-49fe-bfd1-f8634ccec604	john.doe	2023-06-25 23:05:57.463	john.doe	2023-06-25 23:05:57.463	t	0	iPhone 10, 128GB WHITE	12345	iPhone 10	65000.00
8b1458b1-5aa9-437f-a3de-ea8050ba0e05	john.doe	2023-06-25 23:06:09.566	john.doe	2023-06-25 23:06:09.566	t	0	iPhone 10, 128GB GOLD	12345	iPhone 10	65000.00
d5e3fa1d-86c7-4bf5-985b-af865bea9c07	john.doe	2023-06-25 23:07:23.325	john.doe	2023-06-25 23:07:23.325	t	0	iPhone 11, 256GB BLACK	12345	iPhone 11	75000.00
b7277469-cb79-4746-abae-e595b32a3666	john.doe	2023-06-25 23:07:38.454	john.doe	2023-06-25 23:07:38.454	t	0	iPhone 11, 512GB BLACK	12345	iPhone 11	85000.00
f2cc6216-9d04-4f3f-882f-4f1567a1a449	john.doe	2023-06-25 23:09:04.734	john.doe	2023-06-25 23:09:04.734	t	0	iPhone 12, 512GB BLACK	12345	iPhone 12	95000.00
df4d78c5-9730-45c4-9480-4c8b6dd20a04	john.doe	2023-06-26 15:35:13.336	john.doe	2023-06-26 15:35:13.336	t	0	Samsung Galaxy s19 64GB	12345	Samsung Galaxy s19	45000.00
bc97cbbb-1bea-480f-b29d-c5cbe649fcd8	john.doe	2023-06-26 15:56:30.649	john.doe	2023-06-26 15:56:30.649	t	0	Samsung Galaxy s19 32GB	12345	Samsung Galaxy s19	35000.00
22273a09-ee9e-4e5a-98fa-6bcfcfa97b49	john.doe	2023-05-28 20:26:12.919	john.doe	2023-06-27 10:34:43.158	t	3	iPhone 10, 64 GB	12345	iPhone 10	61504.00
fc4bd3c5-bafa-4f8c-b4d3-cd97aa557590	john.doe	2023-06-27 10:40:58.035	john.doe	2023-06-27 10:40:58.035	t	0	iPhone 15, 512GB	12345	iPhone 15	99999.00
f14b2e2e-7212-471f-8e95-cf1a37e9544d	john.doe	2023-06-28 10:21:29.626	john.doe	2023-06-28 10:21:29.626	t	0	iPhone 14 128 GB SILVER	12345	iPhone 14	80000.00
5dcb87dd-0480-4e45-be83-ddcba3313c4f	john.doe	2023-07-07 12:36:44.929	john.doe	2023-07-07 12:36:44.929	t	0	Apple Watch series 5	12345	Apple Watch 5	40000.00
25886064-2a6d-417a-9040-111166f250f3	john.doe	2023-07-07 12:36:59.464	john.doe	2023-07-07 12:36:59.464	t	0	Apple Watch series 6	12345	Apple Watch 6	45000.00
2cdf4620-1b74-4e6d-a525-e1b7baeb1aec	john.doe	2023-07-07 12:37:15.797	john.doe	2023-07-07 12:37:15.797	t	0	Apple Watch series 7	12345	Apple Watch 7	50000.00
\.


--
-- Data for Name: reservation_flight_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.reservation_flight_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, airlines, flightid, flightreservationno, fromcity, journeydate, pnr, rate, reservationstatus, statusreasons, tocity, reservation_id, gender, passengername) FROM stdin;
067514b4-20a6-4a66-bf88-54ea05afa02d	anonymousUser	2023-07-12 23:17:41.92806	t	anonymousUser	2023-07-12 23:17:41.92806	0	Delta	KOC-BAN	string	KOC	2023-07-31	pnr65321	5000	RESERVATION_REQUEST_RECEIVED	string	BAN	b9176b11-d924-495e-a459-3b17687d773e	F	Jane Doe
85697f23-3b3f-461b-aea9-f31c06907917	anonymousUser	2023-07-12 23:17:41.927086	t	anonymousUser	2023-07-12 23:17:41.927086	0	Delta	BAN-KOC	string	BAN	2023-07-28	pnr12356	5000	RESERVATION_REQUEST_RECEIVED	string	KOC	b9176b11-d924-495e-a459-3b17687d773e	F	Jane Doe
f64a1d45-8ec9-4022-8fc4-2180de771ead	anonymousUser	2023-07-12 23:17:41.927086	t	anonymousUser	2023-07-12 23:17:41.927086	0	Delta	BAN-KOC	string	BAN	2023-07-28	pnr12356	5000	RESERVATION_REQUEST_RECEIVED	string	KOC	b9176b11-d924-495e-a459-3b17687d773e	M	John Doe
fb948edc-a0ec-4d46-a437-3322d08f3752	anonymousUser	2023-07-12 23:17:41.92806	t	anonymousUser	2023-07-12 23:17:41.92806	0	Delta	KOC-BAN	string	KOC	2023-07-31	pnr65321	5000	RESERVATION_REQUEST_RECEIVED	string	BAN	b9176b11-d924-495e-a459-3b17687d773e	M	John Doe
dfba3728-f6fa-4e43-a604-446989a27473	anonymousUser	2023-07-17 14:13:10.257117	t	anonymousUser	2023-07-17 14:13:10.257117	0	Vistara	KOC-HYD-123		KOC	2023-07-28	pnr65322	5000	RESERVATION_REQUEST_RECEIVED	string	HYD	7be6cf96-70bd-4384-98d3-3a8bd5708501	F	Jane Doe
be9ae8a1-8694-4f99-88fc-c1eef7994a57	anonymousUser	2023-07-17 14:13:10.257402	t	anonymousUser	2023-07-17 14:13:10.257402	0	Vistara	HYD-KOC-321		HYD	2023-07-31	pnr65322	5000	RESERVATION_REQUEST_RECEIVED	string	KOC	7be6cf96-70bd-4384-98d3-3a8bd5708501	F	Jane Doe
\.


--
-- Data for Name: reservation_hotel_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.reservation_hotel_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, days, enddate, hotelid, hotelname, hotelreservationno, persons, rate, reservationstatus, startdate, reservation_id) FROM stdin;
759d0604-c0cf-4e68-8d3d-e1db250c15d8	anonymousUser	2023-07-12 23:17:41.932012	t	anonymousUser	2023-07-12 23:17:41.932012	0	3	2023-07-31	taj-kochi	Hotel Taj Palace, KOCHI, Kerala	123456	2	21500	RESERVATION_REQUEST_RECEIVED	2023-07-28	b9176b11-d924-495e-a459-3b17687d773e
4f5fa6b3-2f4b-4018-829b-a634015cc05d	anonymousUser	2023-07-17 13:20:57.238046	t	anonymousUser	2023-07-17 13:20:57.238046	0	2	2023-07-31	Holiday-Inn-kochi	Hotel Holiday-Inn, KOCHI, Kerala	123457	1	15000	RESERVATION_REQUEST_RECEIVED	2023-07-28	3d256890-52bb-4b5f-afa5-c978fa6cabfd
\.


--
-- Data for Name: reservation_payment_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.reservation_payment_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, status, transaction_id) FROM stdin;
ab4c885f-24cf-4f1d-8854-0e4a905f46e1	anonymousUser	2023-07-12 23:17:41.917495	t	anonymousUser	2023-07-12 23:17:41.917495	0	string	string
1385e871-256f-43ae-ad6c-a903bfd1ace7	anonymousUser	2023-07-17 12:59:54.080742	t	anonymousUser	2023-07-17 12:59:54.080742	0	string	string
916d5ae4-f786-431b-92e2-266379fe613c	anonymousUser	2023-07-17 13:20:57.223166	t	anonymousUser	2023-07-17 13:20:57.223166	0	string	string
9c16eb4a-f9f7-429c-9a13-3b65c1fb799c	anonymousUser	2023-07-17 14:13:10.244575	t	anonymousUser	2023-07-17 14:13:10.244575	0	string	string
7ee53b4f-df5c-4e85-bc0e-550e47b69b86	anonymousUser	2023-07-17 16:47:43.634659	t	anonymousUser	2023-07-17 16:47:43.634659	0	string	string
\.


--
-- Data for Name: reservation_rental_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.reservation_rental_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, days, enddate, rate, rentalid, rentalname, rentalreservationno, rentaltype, rentalvehiclelicense, reservationstatus, startdate, statusreasons, reservation_id, primarydriver) FROM stdin;
81cf8f66-69ab-4b49-8dcc-8f6739e4b13b	anonymousUser	2023-07-12 23:17:41.93635	t	anonymousUser	2023-07-12 23:17:41.93635	0	3	2023-07-31	15000	scorpio-2023	Top Cars Rental - Scorpio	9876543	SUV	KL-05-Y-2023	RESERVATION_REQUEST_RECEIVED	2023-07-28		b9176b11-d924-495e-a459-3b17687d773e	Jane Doe
b7557130-b45f-489b-a1da-0e7f23ee35f8	anonymousUser	2023-07-17 16:47:43.639294	t	anonymousUser	2023-07-17 16:47:43.639294	0	5	2023-07-30	17000	XUV-700-2023	Top Cars Rental - XUV 700	7879543	SUV	KL-05-Y-2022	RESERVATION_REQUEST_RECEIVED	2023-07-25		a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7	Ann Doe
\.


--
-- Data for Name: reservation_state_history_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.reservation_state_history_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, aggregateversionno, notes, sourcestate, targetstate, transitionevent, reservation_id) FROM stdin;
95d13440-3b3c-4ce6-8989-8be54717d408	anonymousUser	2023-07-17 16:56:05.214843	t	anonymousUser	2023-07-17 16:56:05.214843	0	6062		RESERVATION_REQUEST_RECEIVED	RESERVATION_INITIALIZED	RESERVATION_VALIDATION_EVENT	b9176b11-d924-495e-a459-3b17687d773e
50197237-762f-4230-8d87-cadc6b76525f	anonymousUser	2023-07-17 16:56:05.259146	t	anonymousUser	2023-07-17 16:56:05.259146	0	6063		RESERVATION_INITIALIZED	IN_PROGRESS	RESERVATION_INIT_EVENT	b9176b11-d924-495e-a459-3b17687d773e
74ce1da1-ede3-4378-a954-936585d21c8d	anonymousUser	2023-07-17 16:56:05.318216	t	anonymousUser	2023-07-17 16:56:05.318216	0	6065		IN_PROGRESS	HOTEL_BOOKING_REQUEST	HOTEL_BOOKING_REQUEST_EVENT	b9176b11-d924-495e-a459-3b17687d773e
2e9cfeeb-2aa7-41e4-bd3b-42d94b8c7246	anonymousUser	2023-07-17 16:56:05.359667	t	anonymousUser	2023-07-17 16:56:05.359667	0	6066		HOTEL_BOOKING_REQUEST	HOTEL_BOOKING_CONFIRMED	HOTEL_BOOKING_CONFIRMED_EVENT	b9176b11-d924-495e-a459-3b17687d773e
b47640a1-2b05-4a12-aa79-af375b72f24c	anonymousUser	2023-07-17 16:56:05.414559	t	anonymousUser	2023-07-17 16:56:05.414559	0	6067		HOTEL_BOOKING_CONFIRMED	RENTAL_BOOKING_REQUEST	RENTAL_BOOKING_REQUEST_EVENT	b9176b11-d924-495e-a459-3b17687d773e
bf71d0cf-6b11-4d33-a0c1-5c8788d57800	anonymousUser	2023-07-17 16:56:05.449165	t	anonymousUser	2023-07-17 16:56:05.449165	0	6068		RENTAL_BOOKING_REQUEST	RENTAL_BOOKING_CONFIRMED	RENTAL_BOOKING_CONFIRMED_EVENT	b9176b11-d924-495e-a459-3b17687d773e
34160fec-2e55-4252-8c88-dca5e89607d0	anonymousUser	2023-07-17 16:56:05.492327	t	anonymousUser	2023-07-17 16:56:05.492327	0	6069		RENTAL_BOOKING_CONFIRMED	FLIGHT_BOOKING_REQUEST	FLIGHT_BOOKING_REQUEST_EVENT	b9176b11-d924-495e-a459-3b17687d773e
31b76834-b5f3-4862-81ad-f2ff554450c3	anonymousUser	2023-07-17 16:56:05.528299	t	anonymousUser	2023-07-17 16:56:05.528299	0	6070		FLIGHT_BOOKING_REQUEST	FLIGHT_BOOKING_CONFIRMED	FLIGHT_BOOKING_CONFIRMED_EVENT	b9176b11-d924-495e-a459-3b17687d773e
b42fedf8-f37e-4989-ab53-07281970e7a9	anonymousUser	2023-07-17 16:56:05.56598	t	anonymousUser	2023-07-17 16:56:05.56598	0	6071		FLIGHT_BOOKING_CONFIRMED	PAYMENT_REQUEST_INIT	PAYMENT_REQUEST_EVENT	b9176b11-d924-495e-a459-3b17687d773e
6560f5b9-043b-4f0f-a20b-37114d93623e	anonymousUser	2023-07-17 16:56:05.60041	t	anonymousUser	2023-07-17 16:56:05.60041	0	6072		PAYMENT_REQUEST_INIT	PAYMENT_DECLINED	PAYMENT_DECLINED_EVENT	b9176b11-d924-495e-a459-3b17687d773e
360aa63f-576b-4b5e-b316-a068295ad372	anonymousUser	2023-07-17 16:56:05.749482	t	anonymousUser	2023-07-17 16:56:05.749482	0	6073		PAYMENT_DECLINED	TRIP_CANCELLED	TRIP_CANCELLED_EVENT	b9176b11-d924-495e-a459-3b17687d773e
36586fc4-297a-4818-a393-9b2e869189e3	anonymousUser	2023-07-17 16:56:05.753881	t	anonymousUser	2023-07-17 16:56:05.753881	0	6074		TRIP_CANCELLED	FLIGHT_BOOKING_ROLLBACK	FLIGHT_ROLLBACK_EVENT	b9176b11-d924-495e-a459-3b17687d773e
740da144-9de9-441e-b0c5-b045bb02aeff	anonymousUser	2023-07-17 16:56:05.75746	t	anonymousUser	2023-07-17 16:56:05.75746	0	6075		FLIGHT_BOOKING_ROLLBACK	RENTAL_BOOKING_ROLLBACK	RENTAL_ROLLBACK_EVENT	b9176b11-d924-495e-a459-3b17687d773e
652c8464-c987-4e62-8729-0f82d949b896	anonymousUser	2023-07-17 16:56:05.761349	t	anonymousUser	2023-07-17 16:56:05.761349	0	6076		RENTAL_BOOKING_ROLLBACK	HOTEL_BOOKING_ROLLBACK	HOTEL_ROLLBACK_EVENT	b9176b11-d924-495e-a459-3b17687d773e
d52f9118-3142-41b5-9104-e8a2a90bc42b	anonymousUser	2023-07-17 16:56:05.766154	t	anonymousUser	2023-07-17 16:56:05.766154	0	6077		HOTEL_BOOKING_ROLLBACK	ROLLBACK	ROLLBACK_EVENT	b9176b11-d924-495e-a459-3b17687d773e
353681dc-cf84-414d-ae14-bc23828fea37	anonymousUser	2023-07-17 16:56:05.771687	t	anonymousUser	2023-07-17 16:56:05.771687	0	6078		ROLLBACK	RESERVATION_TERMINATED	AUTO_TRANSITION_EVENT	b9176b11-d924-495e-a459-3b17687d773e
f6be7272-6b10-4d5b-ac2d-22be3d1c4238	anonymousUser	2023-07-17 16:56:52.769316	t	anonymousUser	2023-07-17 16:56:52.769316	0	87		RESERVATION_REQUEST_RECEIVED	RESERVATION_INITIALIZED	RESERVATION_VALIDATION_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
9c59fa8a-c746-4110-b2d9-d7946ad69daf	anonymousUser	2023-07-17 16:56:52.805425	t	anonymousUser	2023-07-17 16:56:52.805425	0	88		RESERVATION_INITIALIZED	IN_PROGRESS	RESERVATION_INIT_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
26305da1-0fab-4d78-8ec4-371aa7b378de	anonymousUser	2023-07-17 16:56:52.851645	t	anonymousUser	2023-07-17 16:56:52.851645	0	90		IN_PROGRESS	HOTEL_BOOKING_REQUEST	HOTEL_BOOKING_REQUEST_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
153f4ab2-a757-49e9-a73d-8d1c19a173ce	anonymousUser	2023-07-17 16:56:52.904604	t	anonymousUser	2023-07-17 16:56:52.904604	0	91		HOTEL_BOOKING_REQUEST	HOTEL_BOOKING_CONFIRMED	HOTEL_BOOKING_CONFIRMED_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
f8dc5a4f-d7df-4e91-9219-8249094cfa88	anonymousUser	2023-07-17 16:56:52.972061	t	anonymousUser	2023-07-17 16:56:52.972061	0	92		PAYMENT_REQUEST_INIT	PAYMENT_APPROVED	PAYMENT_CONFIRMED_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
9582052d-3717-4d5b-8830-8f31c9fe2320	anonymousUser	2023-07-17 16:56:53.007097	t	anonymousUser	2023-07-17 16:56:53.007097	0	93		PAYMENT_APPROVED	SEND_INVOICE	SEND_INVOICE_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
7de944b5-c7c1-404f-96f9-7e90a2020afa	anonymousUser	2023-07-17 16:56:53.058448	t	anonymousUser	2023-07-17 16:56:53.058448	0	94		SEND_INVOICE	SEND_TRAVEL_DETAILS	SEND_TRAVEL_DETAILS_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
77403584-b154-4166-bd91-9c759c086820	anonymousUser	2023-07-17 16:56:53.093316	t	anonymousUser	2023-07-17 16:56:53.093316	0	95		SEND_TRAVEL_DETAILS	TRIP_CONFIRMED	TRIP_CONFIRMED_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
e689d7fb-5225-4122-9be2-936666f2c937	anonymousUser	2023-07-17 16:56:53.098627	t	anonymousUser	2023-07-17 16:56:53.098627	0	96		TRIP_CONFIRMED	RESERVATION_COMPLETED	AUTO_TRANSITION_EVENT	3d256890-52bb-4b5f-afa5-c978fa6cabfd
0bbe9e28-7be9-4ef9-89ab-9ca07262c765	anonymousUser	2023-07-17 16:59:01.152988	t	anonymousUser	2023-07-17 16:59:01.152988	0	43		FLIGHT_BOOKING_REQUEST	FLIGHT_BOOKING_CONFIRMED	FLIGHT_BOOKING_CONFIRMED_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
8230ed96-8bca-44d7-b91f-642c72d81219	anonymousUser	2023-07-17 16:59:01.188609	t	anonymousUser	2023-07-17 16:59:01.188609	0	44		FLIGHT_BOOKING_CONFIRMED	PAYMENT_REQUEST_INIT	PAYMENT_REQUEST_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
73dd4fa8-ed56-4007-b5e8-80584a523ac2	anonymousUser	2023-07-17 16:59:01.223208	t	anonymousUser	2023-07-17 16:59:01.223208	0	45		PAYMENT_REQUEST_INIT	PAYMENT_APPROVED	PAYMENT_CONFIRMED_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
61602134-1547-4adf-8824-abfa4cc1b3a2	anonymousUser	2023-07-17 16:59:01.259021	t	anonymousUser	2023-07-17 16:59:01.259021	0	46		PAYMENT_APPROVED	SEND_INVOICE	SEND_INVOICE_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
972c7891-2223-402e-913a-12a7e873dd72	anonymousUser	2023-07-17 16:59:01.302087	t	anonymousUser	2023-07-17 16:59:01.302087	0	47		SEND_INVOICE	SEND_TRAVEL_DETAILS	SEND_TRAVEL_DETAILS_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
113109f1-7a84-43f9-a2e4-0555d8463577	anonymousUser	2023-07-17 16:59:01.333896	t	anonymousUser	2023-07-17 16:59:01.333896	0	48		SEND_TRAVEL_DETAILS	TRIP_CONFIRMED	TRIP_CONFIRMED_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
3dd8b258-9c95-4343-b00e-cddf300671fc	anonymousUser	2023-07-17 16:59:01.340041	t	anonymousUser	2023-07-17 16:59:01.340041	0	49		TRIP_CONFIRMED	RESERVATION_COMPLETED	AUTO_TRANSITION_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
4c03e6f2-700c-4ddc-9089-befb30176e4e	anonymousUser	2023-07-17 16:59:24.565544	t	anonymousUser	2023-07-17 16:59:24.565544	0	13		RESERVATION_REQUEST_RECEIVED	RESERVATION_INITIALIZED	RESERVATION_VALIDATION_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
a10a87c2-1f10-43a5-991e-57d26a6d3be5	anonymousUser	2023-07-17 16:59:01.001697	t	anonymousUser	2023-07-17 16:59:01.001697	0	39		RESERVATION_REQUEST_RECEIVED	RESERVATION_INITIALIZED	RESERVATION_VALIDATION_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
427fbeda-91c8-4897-a390-0596059a5e99	anonymousUser	2023-07-17 16:59:01.049896	t	anonymousUser	2023-07-17 16:59:01.049896	0	40		RESERVATION_INITIALIZED	IN_PROGRESS	RESERVATION_INIT_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
df6261d8-1533-4748-9bfe-89250ccbead5	anonymousUser	2023-07-17 16:59:24.597887	t	anonymousUser	2023-07-17 16:59:24.597887	0	14		RESERVATION_INITIALIZED	IN_PROGRESS	RESERVATION_INIT_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
cb7187dc-76a2-471a-9786-b20109b08f86	anonymousUser	2023-07-17 16:59:01.101381	t	anonymousUser	2023-07-17 16:59:01.101381	0	42		IN_PROGRESS	FLIGHT_BOOKING_REQUEST	FLIGHT_BOOKING_REQUEST_EVENT	7be6cf96-70bd-4384-98d3-3a8bd5708501
fc086766-3e82-4549-979c-ae1f70f8540e	anonymousUser	2023-07-17 16:59:24.655794	t	anonymousUser	2023-07-17 16:59:24.655794	0	16		IN_PROGRESS	RENTAL_BOOKING_REQUEST	RENTAL_BOOKING_REQUEST_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
3850cbe1-626f-4302-b159-66cb78f9e75b	anonymousUser	2023-07-17 16:59:24.68777	t	anonymousUser	2023-07-17 16:59:24.68777	0	17		RENTAL_BOOKING_REQUEST	RENTAL_BOOKING_CONFIRMED	RENTAL_BOOKING_CONFIRMED_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
ac5a5394-d54f-4f39-921c-166abf78d2a9	anonymousUser	2023-07-17 16:59:24.753466	t	anonymousUser	2023-07-17 16:59:24.753466	0	18		PAYMENT_REQUEST_INIT	PAYMENT_APPROVED	PAYMENT_CONFIRMED_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
b659032e-9b29-4ab3-9e81-d9a42d3d6e58	anonymousUser	2023-07-17 16:59:24.788993	t	anonymousUser	2023-07-17 16:59:24.788993	0	19		PAYMENT_APPROVED	SEND_INVOICE	SEND_INVOICE_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
fbff0d87-62ce-421b-b440-e835b38425d8	anonymousUser	2023-07-17 16:59:24.822485	t	anonymousUser	2023-07-17 16:59:24.822485	0	20		SEND_INVOICE	SEND_TRAVEL_DETAILS	SEND_TRAVEL_DETAILS_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
72c4c9a9-2d46-4544-8293-71a16f34fba2	anonymousUser	2023-07-17 16:59:24.855327	t	anonymousUser	2023-07-17 16:59:24.855327	0	21		SEND_TRAVEL_DETAILS	TRIP_CONFIRMED	TRIP_CONFIRMED_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
1d01e067-cdf1-45e8-a95f-f10742590310	anonymousUser	2023-07-17 16:59:24.859437	t	anonymousUser	2023-07-17 16:59:24.859437	0	22		TRIP_CONFIRMED	RESERVATION_COMPLETED	AUTO_TRANSITION_EVENT	a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7
\.


--
-- Data for Name: reservation_tx; Type: TABLE DATA; Schema: ms_schema; Owner: postgres
--

COPY ms_schema.reservation_tx (uuid, createdby, createdtime, isactive, updatedby, updatedtime, version, currency, city, country, landmark, phone, state, street, zip_code, customer_id, reservationstatus, result, totalvalue, payment_id, rollbackonfailure) FROM stdin;
a74d7ab4-d7d1-4c8a-aa69-2de50d4127e7	anonymousUser	2023-07-17 16:47:43.629823	t	anonymousUser	2023-07-17 16:59:24.860479	22	INR	East Hartford	USA		732-627-0005	CT	13 Edgebrook Dr	082321	456	RESERVATION_COMPLETED	TRIP_CONFIRMED	85000	7ee53b4f-df5c-4e85-bc0e-550e47b69b86	f
3d256890-52bb-4b5f-afa5-c978fa6cabfd	anonymousUser	2023-07-17 13:20:57.232586	t	anonymousUser	2023-07-17 16:56:53.099561	96	INR	West Hartford	USA		732-236-0001	CT	5 Edgebrook Drive	08311	234	RESERVATION_COMPLETED	TRIP_CONFIRMED	30000	916d5ae4-f786-431b-92e2-266379fe613c	f
b9176b11-d924-495e-a459-3b17687d773e	anonymousUser	2023-07-12 23:17:41.921865	t	anonymousUser	2023-07-17 16:56:05.77228	6078	INR	Edison	USA	Walmart	732-236-0000	NJ	321 Cobblestone Ln	08211	123	RESERVATION_TERMINATED	ROLLBACK	119500	ab4c885f-24cf-4f1d-8854-0e4a905f46e1	f
7be6cf96-70bd-4384-98d3-3a8bd5708501	anonymousUser	2023-07-17 14:13:10.245199	t	anonymousUser	2023-07-17 16:59:01.343785	49	INR	West Hartford	USA		732-627-0003	CT	5 Edgebrook Dr	082201	345	RESERVATION_COMPLETED	TRIP_CONFIRMED	10000	9c16eb4a-f9f7-429c-9a13-3b65c1fb799c	f
\.


--
-- Name: carts_tx carts_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.carts_tx
    ADD CONSTRAINT carts_tx_pkey PRIMARY KEY (uuid);


--
-- Name: country_geolite_m country_geolite_m_pk; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.country_geolite_m
    ADD CONSTRAINT country_geolite_m_pk PRIMARY KEY (geoname_id);


--
-- Name: country_t country_t_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: arafkarsh
--

ALTER TABLE ONLY ms_schema.country_t
    ADD CONSTRAINT country_t_pkey PRIMARY KEY (cid);


--
-- Name: order_item_tx order_item_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.order_item_tx
    ADD CONSTRAINT order_item_tx_pkey PRIMARY KEY (uuid);


--
-- Name: order_state_history_tx order_state_history_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.order_state_history_tx
    ADD CONSTRAINT order_state_history_tx_pkey PRIMARY KEY (uuid);


--
-- Name: order_tx order_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.order_tx
    ADD CONSTRAINT order_tx_pkey PRIMARY KEY (uuid);


--
-- Name: order_payment_tx payment_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.order_payment_tx
    ADD CONSTRAINT payment_tx_pkey PRIMARY KEY (uuid);


--
-- Name: products_m products_m_pkey1; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.products_m
    ADD CONSTRAINT products_m_pkey1 PRIMARY KEY (uuid);


--
-- Name: reservation_flight_tx reservation_flight_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_flight_tx
    ADD CONSTRAINT reservation_flight_tx_pkey PRIMARY KEY (uuid);


--
-- Name: reservation_hotel_tx reservation_hotel_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_hotel_tx
    ADD CONSTRAINT reservation_hotel_tx_pkey PRIMARY KEY (uuid);


--
-- Name: reservation_payment_tx reservation_payment_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_payment_tx
    ADD CONSTRAINT reservation_payment_tx_pkey PRIMARY KEY (uuid);


--
-- Name: reservation_rental_tx reservation_rental_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_rental_tx
    ADD CONSTRAINT reservation_rental_tx_pkey PRIMARY KEY (uuid);


--
-- Name: reservation_state_history_tx reservation_state_history_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_state_history_tx
    ADD CONSTRAINT reservation_state_history_tx_pkey PRIMARY KEY (uuid);


--
-- Name: reservation_tx reservation_tx_pkey; Type: CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_tx
    ADD CONSTRAINT reservation_tx_pkey PRIMARY KEY (uuid);


--
-- Name: order_tx fk17hk2k77tycbvnfa968wayqhd; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.order_tx
    ADD CONSTRAINT fk17hk2k77tycbvnfa968wayqhd FOREIGN KEY (payment_id) REFERENCES ms_schema.order_payment_tx(uuid);


--
-- Name: reservation_state_history_tx fk32qdu50sncuwuvrbg2xpayrl6; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_state_history_tx
    ADD CONSTRAINT fk32qdu50sncuwuvrbg2xpayrl6 FOREIGN KEY (reservation_id) REFERENCES ms_schema.reservation_tx(uuid);


--
-- Name: reservation_flight_tx fk3lysfi8sy0jvn3o6oc2ofxp7b; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_flight_tx
    ADD CONSTRAINT fk3lysfi8sy0jvn3o6oc2ofxp7b FOREIGN KEY (reservation_id) REFERENCES ms_schema.reservation_tx(uuid);


--
-- Name: order_item_tx fkb3heb7c2x68gtl42n66w217vl; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.order_item_tx
    ADD CONSTRAINT fkb3heb7c2x68gtl42n66w217vl FOREIGN KEY (order_id) REFERENCES ms_schema.order_tx(uuid);


--
-- Name: reservation_tx fkmoijwjheogwtpgsb3fvdafeae; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_tx
    ADD CONSTRAINT fkmoijwjheogwtpgsb3fvdafeae FOREIGN KEY (payment_id) REFERENCES ms_schema.reservation_payment_tx(uuid);


--
-- Name: reservation_hotel_tx fkp04i0v93k4bf80mqgqg4nd7kg; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_hotel_tx
    ADD CONSTRAINT fkp04i0v93k4bf80mqgqg4nd7kg FOREIGN KEY (reservation_id) REFERENCES ms_schema.reservation_tx(uuid);


--
-- Name: order_state_history_tx fkqm01yul77rf1ekm5seeqxnaqp; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.order_state_history_tx
    ADD CONSTRAINT fkqm01yul77rf1ekm5seeqxnaqp FOREIGN KEY (order_id) REFERENCES ms_schema.order_tx(uuid);


--
-- Name: reservation_rental_tx fkto3ubx8pguu6nbig3d50lsb83; Type: FK CONSTRAINT; Schema: ms_schema; Owner: postgres
--

ALTER TABLE ONLY ms_schema.reservation_rental_tx
    ADD CONSTRAINT fkto3ubx8pguu6nbig3d50lsb83 FOREIGN KEY (reservation_id) REFERENCES ms_schema.reservation_tx(uuid);


--
-- PostgreSQL database dump complete
--

