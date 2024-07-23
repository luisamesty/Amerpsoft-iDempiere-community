UPDATE amn_concept_types_acct as acta
   SET  
       amn_cre_acct=acty.amn_cre_acct, 
       amn_cre_dw_acct=acty.amn_cre_dw_acct,
       amn_cre_iw_acct=acty.amn_cre_iw_acct, 
       amn_cre_mw_acct=acty.amn_cre_mw_acct, 
       amn_cre_sw_acct=acty.amn_cre_sw_acct,
       amn_deb_acct=acty.amn_deb_acct, 
       amn_deb_dw_acct=acty.amn_deb_dw_acct, 
       amn_deb_iw_acct=acty.amn_deb_iw_acct, 
       amn_deb_mw_acct=acty.amn_deb_mw_acct, 
       amn_deb_sw_acct=acty.amn_deb_sw_acct, 
       amn_concept_types_id = acty.amn_concept_types_id, 
       c_acctschema_id=1000000
   FROM( SELECT amn_cre_acct, amn_cre_dw_acct, amn_cre_iw_acct, amn_cre_mw_acct, amn_cre_sw_acct, 
          amn_deb_acct, amn_deb_dw_acct, amn_deb_iw_acct, amn_deb_mw_acct, amn_deb_sw_acct, 
          amn_concept_types_id, 1000000
      FROM amn_concept_types
   ) AS acty
 WHERE acta.amn_concept_types_id = acty.amn_concept_types_id
 AND acta.c_acctschema_id=1000000