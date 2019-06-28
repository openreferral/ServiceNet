import './shelter-element.scss';

import React from 'react';
import { Row, Col, Card, CardText, CardBody, Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import { IShelter } from 'app/shared/model/shelter.model';
import { Translate, translate } from 'react-jhipster';

export interface IShelterElementProps {
  shelter: IShelter;
}

export interface IShelterElementState {
  open: boolean;
}

export class ShelterElement extends React.Component<IShelterElementProps, IShelterElementState> {
  state: IShelterElementState = {
    open: false
  };

  toggle = (event: any) => {
    const open = !this.state.open;
    this.setState({ open });
  };

  render() {
    const { shelter } = this.props;
    return (
      <Row className="shelter-row">
        <Col>
          <Link to={`#`} className="alert-link" onClick={this.toggle}>
            <Card className="shelter-card">
              <CardBody className="shelter-card-body">
                <div className="shelter-left-container">
                  <CardText className="shelter-left-card-text">
                    <div>{shelter.agencyName}</div>
                    <div>{shelter.programName}</div>
                  </CardText>
                </div>
                <div className="shelter-right-container">
                  <CardText className="shelter-right-card-info">
                    <div>
                      <Translate contentKey="serviceNetApp.shelter.home.card.bedsAvailable" />:{' '}
                      {shelter.beds && shelter.beds.availableBeds
                        ? shelter.beds.availableBeds
                        : translate('serviceNetApp.shelter.home.card.notAvailable')}
                    </div>
                    <div>
                      <Translate contentKey="serviceNetApp.shelter.home.card.waitlist" />:{' '}
                      {shelter.beds && shelter.beds.waitlist
                        ? shelter.beds.waitlist
                        : translate('serviceNetApp.shelter.home.card.notAvailable')}
                    </div>
                    <div className="shelter-expand-label">
                      {this.state.open ? (
                        <span>
                          <Translate contentKey="serviceNetApp.shelter.home.card.lessInfo" /> ▲
                        </span>
                      ) : (
                        <span>
                          <Translate contentKey="serviceNetApp.shelter.home.card.moreInfo" /> ▼
                        </span>
                      )}
                    </div>
                  </CardText>
                </div>
              </CardBody>
            </Card>
          </Link>
          {this.state.open && (
            <Card className="shelter-card-expanded">
              <CardBody>
                <CardText>
                  <p>
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.eligibilityRequirements" />:
                    </div>
                    <div>{shelter.eligibilityDetails}</div>
                    <div>{shelter.documentsRequired}</div>
                  </p>
                  <p>
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.process" />:
                    </div>
                    <div>{shelter.applicationProcess}</div>
                  </p>
                  <p>
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.contactInfo" />:
                    </div>
                    {shelter.emails ? shelter.emails.map((val, j) => <div key={j}>{val}</div>) : null}
                    {shelter.phones
                      ? shelter.phones.map((val, j) => (
                          <div key={j}>
                            {val.number} {val.type ? '(' + val.type + ')' : ''}
                          </div>
                        ))
                      : null}
                  </p>
                  <p>
                    <div className="shelter-info-header">
                      <Translate contentKey="serviceNetApp.shelter.home.card.address" />:
                    </div>
                    <div>{shelter.address1}</div>
                    <div>{shelter.address2}</div>
                    <div>{shelter.city}</div>
                    <div>{shelter.zipcode}</div>
                  </p>
                  <p>
                    <Button tag={Link} to={`/shelter/${shelter.id}`} replace color="info">
                      <span className="d-none d-md-inline">
                        <Translate contentKey="serviceNetApp.shelter.home.card.moreInfo">More info</Translate>
                      </span>
                    </Button>
                  </p>
                </CardText>
              </CardBody>
            </Card>
          )}
        </Col>
      </Row>
    );
  }
}

export default ShelterElement;
