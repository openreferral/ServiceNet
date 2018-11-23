import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './language.reducer';
import { ILanguage } from 'app/shared/model/language.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILanguageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class LanguageDetail extends React.Component<ILanguageDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { languageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.language.detail.title">Language</Translate> [<b>{languageEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="language">
                <Translate contentKey="serviceNetApp.language.language">Language</Translate>
              </span>
            </dt>
            <dd>{languageEntity.language}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.language.srvc">Srvc</Translate>
            </dt>
            <dd>{languageEntity.srvcName ? languageEntity.srvcName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.language.location">Location</Translate>
            </dt>
            <dd>{languageEntity.locationName ? languageEntity.locationName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/language" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/language/${languageEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ language }: IRootState) => ({
  languageEntity: language.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LanguageDetail);
